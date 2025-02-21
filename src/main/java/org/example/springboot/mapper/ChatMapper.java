package org.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.springboot.pojo.ChatConversation;
import org.example.springboot.pojo.ChatMessage;

import java.util.List;

@Mapper
public interface ChatMapper {
    // 会话相关
    int insertConversation(ChatConversation conversation);
    List<ChatConversation> selectConversationsByUserId(Integer userId);
    ChatConversation selectConversationById(Long id);
    int updateConversation(ChatConversation conversation);

    // 消息相关
    int insertMessage(ChatMessage message);
    List<ChatMessage> selectMessagesByConversationId(Long conversationId);
    ChatMessage selectLastMessageByConversationId(Long conversationId);
}