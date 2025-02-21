package org.example.springboot.pojo;

import lombok.Data;

@Data
public class Character {
    private Integer id;
    private Integer user_id;
    private String name;
    private String biography;
    private String avatar_url;
    private Integer fans_count;
    private Integer follow_count;
}
