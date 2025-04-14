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

//    @GetMapping("/person/{id}")
//    public Post getPostById(@PathVariable Integer id) {
//        return postService.getPostById(id);
//    }

    @PostMapping("Update")
    public boolean updatePost(@RequestBody Post post) {
        return postService.updatePost(post);
    }

    @GetMapping("GetAllPosts")
    public Map<String, Object> getPosts(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        // 使用 PageHelper 实现分页和排序
        PageHelper.startPage(pageNum, pageSize, "created_at DESC");  // 合并分页和排序
        List<Post> posts = postService.getAllPostsByPage(pageNum, pageSize).getList();
        PageInfo<Post> pageInfo = new PageInfo<>(posts);
        Map<String, Object> result = new HashMap<>();
        result.put("posts", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return result;
    }

    @GetMapping("GetPosts")
    public Map<String, Object> getPosts(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "status", defaultValue = "0") int status
    ) {
        // 使用 PageHelper 实现分页
        PageHelper.startPage(pageNum, pageSize);
        List<Post> posts = postService.getPostsByPage(pageNum, pageSize,status).getList();
        PageInfo<Post> pageInfo = new PageInfo<>(posts);
        Map<String, Object> result = new HashMap<>();
        result.put("posts", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return result;
    }

    @PostMapping("Insert")
    public ResponseEntity<String> inserpost(
            @RequestParam("user_id") Integer user_id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("status") Integer status){
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
            boolean flag = postService.insertPost(user_id, title, content, image_url,status);
            System.out.println("插入贴子成功");
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

    // 获取待审核的贴子列表
    @GetMapping("/admin/allow")
    public Map<String, Object> getPendingPosts(
            @RequestParam(value = "page", defaultValue = "1") int pageNum,
            @RequestParam(value = "size", defaultValue = "10") int pageSize
    ) {
        // 设置 allow=0 表示获取待审核的帖子
        PageInfo<Post> pageInfo = postService.adminGetAllPosts(pageNum, pageSize, null, null, 0);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return result;
    }

    // 贴子审核
    @PutMapping("/admin/{id}/allow")
    public ResponseEntity<?> reviewPost(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body) {
        Integer allow = body.get("allow");
        if (allow == null || (allow != 1 && allow != 2)) {
            return ResponseEntity.badRequest().build();
        }

        boolean success = postService.updatePostAllow(id, allow);
        return success ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/admin/posts")
    public Map<String, Object> getPosts(
            @RequestParam(value = "page", defaultValue = "1") int pageNum,
            @RequestParam(value = "size", defaultValue = "10") int pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer allow
    ) {
        PageInfo<Post> pageInfo = postService.adminGetAllPosts(pageNum, pageSize, title, status, allow);
        Map<String, Object> result = new HashMap<>();
        result.put("records", pageInfo.getList());
        result.put("total", pageInfo.getTotal());
        result.put("pages", pageInfo.getPages());
        return result;
    }
    
    //贴子列表修改审核状态
    @PutMapping("/admin/posts/{id}/allow")
    public ResponseEntity<?> updatePostAllow(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> body) {
        boolean success = postService.updatePostAllow(id, body.get("allow"));
        return success ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @DeleteMapping("/admin/posts/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Integer id) {
        boolean success = postService.deletePost(id);
        return success ?
                ResponseEntity.ok().build() :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("/admin/posts/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer id) {
        Post post = postService.getPostById(id);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
