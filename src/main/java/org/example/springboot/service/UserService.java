package org.example.springboot.service;

import org.example.springboot.mapper.UserMapper;
import org.example.springboot.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<User> getUser() {
        return userMapper.getUser();
    }

    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }
}
