package org.example.springboot.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Integer id;
    private Integer userId;
    private Integer postId;
    private String content;
    private LocalDateTime createdAt;
    private Integer allow;
    private String characterName;
    private String characterAvatar;
}
