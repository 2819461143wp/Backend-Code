package org.example.springboot.pojo;

import lombok.Data;

@Data
public class Character {
    private Integer id;
    private Integer userId;
    private String name;
    private String biography;
    private String avatarUrl;
    private Integer fansCount;
    private Integer followCount;
}
