package org.example.springboot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import okhttp3.*;
import org.example.springboot.mapper.ChatMapper;
import org.example.springboot.pojo.ChatMessage;
import org.example.springboot.pojo.ChatConversation;
import org.example.springboot.pojo.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ChatService {

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private DocumentIndexService documentIndexService;

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String updateConversationTitle(Long conversationId, String title) {
        try {
            ChatConversation conversation = new ChatConversation();
            conversation.setId(conversationId);
            conversation.setTitle(title);
            conversation.setUpdateTime(LocalDateTime.now());
            chatMapper.updateConversation(conversation);
            return "更新成功";
        } catch (Exception e) {
            return "更新失败: " + e.getMessage();
        }
    }

    @Transactional
    public ChatResponse sendMessage(Integer userId, String message, Long conversationId) {
        try {
            // 如果没有会话ID，创建新会话
            if (conversationId == null) {
                ChatConversation conversation = new ChatConversation();
                conversation.setUserId(userId);
                conversation.setTitle(message.length() > 10 ? message.substring(0, 10) + "..." : message);
                conversation.setCreateTime(LocalDateTime.now());
                conversation.setUpdateTime(LocalDateTime.now());
                chatMapper.insertConversation(conversation);
                conversationId = conversation.getId();
            }
            // 保存用户消息
            ChatMessage userMessage = new ChatMessage();
            userMessage.setUserId(userId);
            userMessage.setConversationId(conversationId);
            userMessage.setRole("user");
            userMessage.setContent(message);
            userMessage.setCreateTime(LocalDateTime.now());
            chatMapper.insertMessage(userMessage);

            // 调用AI API
            String aiResponse = callDeepseekAPI(message,conversationId);

            // 保存AI响应
            ChatMessage aiMessage = new ChatMessage();
            aiMessage.setUserId(userId);
            aiMessage.setConversationId(conversationId);
            aiMessage.setRole("assistant");
            aiMessage.setContent(aiResponse);
            aiMessage.setCreateTime(LocalDateTime.now());
            chatMapper.insertMessage(aiMessage);

            // 更新会话时间
            ChatConversation conversation = new ChatConversation();
            conversation.setId(conversationId);
            conversation.setUpdateTime(LocalDateTime.now());
            chatMapper.updateConversation(conversation);

            // 返回响应
            ChatResponse response = new ChatResponse();
            response.setContent(aiResponse);
            response.setRole("assistant");
            response.setSuccess(true);
            return response;

        } catch (Exception e) {
            ChatResponse response = new ChatResponse();
            response.setSuccess(false);
            response.setErrorMessage("消息发送失败: " + e.getMessage());
            return response;
        }
    }

    private String callDeepseekAPI(String message, Long conversationId) throws Exception {
        // 检索相关文档内容
        List<String> relevantDocs = documentIndexService.searchRelevantContent(message);

        ObjectNode requestBody = objectMapper.createObjectNode();
        ArrayNode messages = requestBody.putArray("messages");

        // 添加系统消息和上下文
        ObjectNode systemMessage = messages.addObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant. " +
                "Please use the following context to answer the question: \n" +
                String.join("\n", relevantDocs));

        // 获取并添加历史消息（最多10条）
        if (conversationId != null) {
            List<ChatMessage> historicalMessages = chatMapper.selectMessagesByConversationId(conversationId);
            int startIndex = Math.max(0, historicalMessages.size() - 10);
            for (int i = startIndex; i < historicalMessages.size(); i++) {
                ChatMessage historicalMessage = historicalMessages.get(i);
                ObjectNode historyNode = messages.addObject();
                historyNode.put("role", historicalMessage.getRole());
                historyNode.put("content", historicalMessage.getContent());
            }
        }

        // 添加当前用户消息
        ObjectNode userMessage = messages.addObject();
        userMessage.put("role", "user");
        userMessage.put("content", message);

        // 设置API请求参数
        requestBody.put("model", "deepseek-chat");
        requestBody.put("frequency_penalty", 0);
        requestBody.put("max_tokens", 2048);
        requestBody.put("presence_penalty", 0);

        // 设置响应格式
        ObjectNode responseFormat = requestBody.putObject("response_format");
        responseFormat.put("type", "text");

        // 设置其他参数
        requestBody.putNull("stop");
        requestBody.put("stream", false);
        requestBody.putNull("stream_options");
        requestBody.put("temperature", 1);
        requestBody.put("top_p", 1);
        requestBody.putNull("tools");
        requestBody.put("tool_choice", "none");
        requestBody.put("logprobs", false);
        requestBody.putNull("top_logprobs");

        // 发送API请求
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());

        Request request = new Request.Builder()
                .url("https://api.deepseek.com/chat/completions")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("API调用失败: " + response.code());
            }

            ObjectNode responseJson = objectMapper.readValue(
                    response.body().string(), ObjectNode.class);
            return responseJson.path("choices").get(0).path("message").path("content").asText();
        }
    }

    public List<ChatConversation> getConversations(Integer userId) {
        return chatMapper.selectConversationsByUserId(userId);
    }

    public List<ChatMessage> getMessages(Long conversationId) {
        return chatMapper.selectMessagesByConversationId(conversationId);
    }
}