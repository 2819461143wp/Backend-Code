package org.example.springboot.service;

import org.example.springboot.mapper.UserMapper;
import org.example.springboot.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;
    public User login(String username, String password) {
        return userMapper.findByUsernameAndPassword(username, password);
    }
    //返回值为用户id
    public Integer register(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userMapper.insertUser(user);
        User u = userMapper.findByUsername(username);
        return u.getId();
    }
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}
