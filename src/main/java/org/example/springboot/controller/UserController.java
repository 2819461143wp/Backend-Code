package org.example.springboot.controller;

import org.example.springboot.pojo.User;
import org.example.springboot.service.CharacterService;
import org.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController{
    @Autowired
    private UserService userService;
    @Autowired
    private CharacterService characterService;

    @PostMapping("/login")
    public String login(@RequestBody Map<String, Object> payload) {
        String username = (String) payload.get("username");
        String password = (String) payload.get("password");
        boolean result = userService.login(username, password);
        return result ? "登录成功" : "登录失败";
    }

    @PostMapping("/register")
    public String register(@RequestBody Map<String, Object> payload) {
        String username = (String) payload.get("username");
        String password = (String) payload.get("password");
        Integer user_id = userService.register(username, password);
        boolean result = characterService.insertCharacter(user_id);
        return result ? "注册成功" : "注册失败";
    }

    @GetMapping("/{username}")
    public User getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }

}