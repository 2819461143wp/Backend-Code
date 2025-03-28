package org.example.springboot.pojo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.ToString;

@Data
public class User {
    private Integer id;
    @ExcelProperty("账号")
    private String username;
    @ExcelProperty("密码")
    private String password;
    private String role;
}
