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
import java.util.ArrayList;

@Service
public class ChatService {

    @Value("${deepseek.api.key}")
    private String apiKey;

    @Autowired
    private ChatMapper chatMapper;

    private final OkHttpClient client = new OkHttpClient();
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
            String aiResponse = callDeepseekAPI(message);

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

    private String callDeepseekAPI(String message) throws Exception {
        // 创建请求体的 JSON 对象
        ObjectNode requestBody = objectMapper.createObjectNode();
        // 创建消息数组
        ArrayNode messages = requestBody.putArray("messages");

        // 添加 system 消息，设定 AI 角色
        ObjectNode systemMessage = messages.addObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "You are a helpful assistant");

        // 添加用户消息
        ObjectNode userMessage = messages.addObject();
        userMessage.put("role", "user");
        userMessage.put("content", message);

        // 设置模型名称
        requestBody.put("model", "deepseek-chat");
        // 设置频率惩罚参数
        requestBody.put("frequency_penalty", 0);
        // 设置最大 token 数
        requestBody.put("max_tokens", 2048);
        // 设置存在惩罚参数
        requestBody.put("presence_penalty", 0);

        // 设置响应格式
        ObjectNode responseFormat = requestBody.putObject("response_format");
        responseFormat.put("type", "text");

        // 设置停止标记为 null
        requestBody.putNull("stop");
        // 设置是否流式传输
        requestBody.put("stream", false);
        // 设置流选项为 null
        requestBody.putNull("stream_options");
        // 设置温度参数
        requestBody.put("temperature", 1);
        // 设置 top_p 参数
        requestBody.put("top_p", 1);
        // 设置工具为 null
        requestBody.putNull("tools");
        // 设置工具选择
        requestBody.put("tool_choice", "none");
        // 设置是否返回 log 概率
        requestBody.put("logprobs", false);
        // 设置 top_logprobs 为 null
        requestBody.putNull("top_logprobs");

        // 设置请求的媒体类型为 JSON
        MediaType mediaType = MediaType.parse("application/json");
        // 创建请求体
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());

        // 构建 HTTP 请求
        Request request = new Request.Builder()
                .url("https://api.deepseek.com/chat/completions")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        // 发送请求并处理响应
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new Exception("API调用失败: " + response.code());
            }

            // 解析响应 JSON
            ObjectNode responseJson = objectMapper.readValue(
                    response.body().string(), ObjectNode.class);
            // 返回 AI 响应内容
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