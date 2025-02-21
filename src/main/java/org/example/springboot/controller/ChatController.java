package org.example.springboot.controller;

import org.example.springboot.pojo.ChatResponse;
import org.example.springboot.pojo.ChatConversation;
import org.example.springboot.pojo.ChatMessage;
import org.example.springboot.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/sendMessage")
    public ResponseEntity<ChatResponse> sendMessage(@RequestParam Integer userId, @RequestParam String message, @RequestParam(required = false) Long conversationId) {
        ChatResponse response = chatService.sendMessage(userId, message, conversationId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ChatConversation>> getConversations(@RequestParam Integer userId) {
        List<ChatConversation> conversations = chatService.getConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/messages")
    public ResponseEntity<List<ChatMessage>> getMessages(@RequestParam Long conversationId) {
        List<ChatMessage> messages = chatService.getMessages(conversationId);
        return ResponseEntity.ok(messages);
    }
}