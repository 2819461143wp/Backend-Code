package org.example.springboot.pojo;

import lombok.Data;
import lombok.ToString;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
}
