package org.example.springboot.pojo;

import lombok.Data;

@Data
public class Post {
    private Integer id;
    private Integer userId;
    private String title;
    private String content;
    private String createdAt;
    private String updatedAt;
    private String isDeleted;
    private String imageUrl;
    private Integer starsCount;
    private Integer status;
    private Integer allow;
}
