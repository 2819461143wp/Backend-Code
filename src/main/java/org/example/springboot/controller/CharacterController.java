package org.example.springboot.controller;

import org.example.springboot.pojo.Character;
import org.example.springboot.service.CharacterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/character")
public class CharacterController{
    @Autowired
    private CharacterService characterService;

    @GetMapping("/{user_id}")
    public Character getCharacterById(@PathVariable Integer user_id) {
        return characterService.getCharacterById(user_id);
    }

    private static final String UPLOAD_DIR = "F:/dachuang/source/images";  // 外部路径


    @PostMapping("/update")
    public ResponseEntity<String> updateProfile(
            @RequestParam("userId") Integer userId,
            @RequestParam("name") String name,
            @RequestParam("biography") String biography,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // 创建上传目录
            File uploadDir = new File(UPLOAD_DIR);
            System.out.println("UPLOAD_DIR 路径: " + uploadDir.getAbsolutePath());
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();  // 如果目录不存在，创建目录
            }
            String avatarUrl = null;
            if (file != null && !file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
                avatarUrl = "images/" + uniqueFilename;  // 保存的路径
                File savedFile = new File(uploadDir, uniqueFilename);
                file.transferTo(savedFile);  // 保存文件
            }
            // 更新用户信息
            boolean flag = characterService.updateCharacter(userId, name, biography, avatarUrl);
            if (!flag) {
                System.out.println("service部分出错了");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("service部分出错了");
            }
            return ResponseEntity.ok("用户信息更新成功");
        } catch (IOException e) {
            System.out.println("未执行try部分");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失败");
        }
    }

}