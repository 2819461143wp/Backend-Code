package org.example.springboot.pojo;

import lombok.Data;

@Data
public class UserStudentData {
    private User user;
    private Student student;
    // 添加构造函数初始化对象
    public UserStudentData() {
        this.user = new User();
        this.student = new Student();
    }
}
