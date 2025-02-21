package org.example.springboot;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@SpringBootTest
class SpringbootApplicationTests {

    @Resource
    JavaMailSender sender;


    @Test
    void contextLoads() {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setSubject("Springboot Mail Test");
        mail.setText("Hello, this is a test mail from Springboot.");
        mail.setTo("2819461143@qq.com");
        mail.setFrom("15990879716@163.com"); // 设置发件人地址
        sender.send(mail);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

}
