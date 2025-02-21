package org.example.springboot.pojo;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Arrays;

@Data
public class ChatMessage {
    private Long id;
    private Integer userId;
    private String role;
    private String content;
    private LocalDateTime createTime;
    private Long conversationId;
}

