package org.example.springboot.pojo;

import lombok.Data;

@Data
public class Post {
    private Integer id;
    private Integer user_id;
    private String title;
    private String content;
    private String created_at;
    private String updated_at;
    private String is_deleted;
    private String image_url;
    private Integer stars_count;
    private Integer status;
}
