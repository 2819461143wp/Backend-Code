package org.example.springboot.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.springboot.pojo.Post;
import org.example.springboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private static final String PostImage = "F:/dachuang/source/posts";  // 外部路径

    @Autowired
    private PostService postService;

    @GetMapping("/person/{id}")
    public Post getPostById(@PathVariable Integer id) {
        return postService.getPostById(id);
    }

    @PostMapping("Update")
    public boolean updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }

    @GetMapping("GetPosts")
    public Map<String, Object> getPosts(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        // 使用 PageHelper 实现分页
        PageHelper.startPage(pageNum, pageSize);
        List<Post> posts = postService.getPostsByPage(pageNum, pageSize).getList();
        PageInfo<Post> pageInfo = new PageInfo<>(posts);
        Map<String, Object> result = new HashMap<>();
        result.put("posts", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        return result;
    }

    @PostMapping("Insert")
    public ResponseEntity<String> inserpost(
            @RequestParam("user_id") Integer user_id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            // 创建上传目录
            File uploadDir = new File(PostImage);
            System.out.println("PostImage 路径: " + uploadDir.getAbsolutePath());
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();  // 如果目录不存在，创建目录
            }
            String image_url = null;
            if (file != null && !file.isEmpty()) {
                String originalFilename = file.getOriginalFilename();
                String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
                image_url = "posts/" + uniqueFilename;  // 保存的路径
                File savedFile = new File(uploadDir, uniqueFilename);
                System.out.println("保存文件路径: " + savedFile.getAbsolutePath());
                file.transferTo(savedFile);  // 保存文件
                System.out.println("文件保存成功");
            } else {
                System.out.println("文件为空或不存在");
            }
            // 更新用户信息
            boolean flag = postService.insertPost(user_id, title, content, image_url);
            if (!flag) {
                System.out.println("service部分出错了");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("service部分出错了");
            }
            return ResponseEntity.ok("发表贴子成功");
        } catch (IOException e) {
            System.out.println("未执行try部分: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("发表贴子失败");
        }
    }
}
