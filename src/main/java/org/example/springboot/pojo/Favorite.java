package org.example.springboot.pojo;

import lombok.Data;

@Data
public class Favorite {
    private Integer id;
    private Integer userId;
    private Integer postId;
}
