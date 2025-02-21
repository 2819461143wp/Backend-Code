package org.example.springboot.pojo;

import lombok.Data;

@Data
public class ChatResponse {
    private String content;
    private String role;
    private boolean success;
    private String errorMessage;
}