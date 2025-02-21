package org.example.springboot.pojo;

import lombok.Data;

@Data
public class ChatRequest {
    private String message;
    private Long conversationId;
}