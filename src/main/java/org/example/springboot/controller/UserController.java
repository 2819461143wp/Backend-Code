package org.example.springboot.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.springboot.pojo.ChatConversation;
import org.example.springboot.pojo.User;
import org.example.springboot.service.CharacterService;
import org.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController{
    @Autowired
    private UserService userService;
    @Autowired
    private CharacterService characterService;

    @PostMapping("/login")
    public User login(@RequestBody Map<String, Object> payload) {
        String username = (String) payload.get("username");
        String password = (String) payload.get("password");
        return userService.login(username, password);
    }

    @PostMapping("/register")
    public String register(@RequestBody Map<String, Object> payload) {
        String username = (String) payload.get("username");
        String password = (String) payload.get("password");
        Integer user_id = userService.register(username, password);
        boolean result = characterService.insertCharacter(user_id);
        return result ? "注册成功" : "注册失败";
    }

    @GetMapping("/getuser")
    public Map<String, Object> getUser(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> users = userService.getUser();
        PageInfo<User> pageInfo = new PageInfo<>(users);
        Map<String, Object> result = new HashMap<>();
        result.put("users", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return result;
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(
            @RequestParam Integer id,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String role) {
        try {
            User user = new User();
            user.setId(id);
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);

            int result = userService.updateUser(user);
            if (result > 0) {
                return ResponseEntity.ok("更新成功");
            } else {
                return ResponseEntity.badRequest().body("更新失败");
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("更新出错：" + e.getMessage());
        }
    }

}