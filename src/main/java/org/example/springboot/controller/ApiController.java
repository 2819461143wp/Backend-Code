//package org.example.springboot.controller;
//
//import org.example.springboot.pojo.Character;
//import org.example.springboot.pojo.Post;
//import org.example.springboot.pojo.User;
//import org.example.springboot.service.CharacterService;
//import org.example.springboot.service.ChatService;
//import org.example.springboot.service.PostService;
//import org.example.springboot.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.Map;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api")
//public class ApiController {
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private PostService postService;
//    @Autowired
//    private CharacterService characterService;
//
//    // 修改为外部可访问的目录
//    private static final String UPLOAD_DIR = "F:/dachuang/source/images";  // 外部路径
//
//
//    @PostMapping("/login")
//    public String login(@RequestBody Map<String, Object> payload) {
//        String username = (String) payload.get("username");
//        String password = (String) payload.get("password");
//        boolean result = userService.login(username, password);
//        return result ? "登录成功" : "登录失败";
//    }
//
//    @PostMapping("/register")
//    public String register(@RequestBody Map<String, Object> payload) {
//        String username = (String) payload.get("username");
//        String password = (String) payload.get("password");
//        Integer user_id = userService.register(username, password);
//        boolean result = characterService.insertCharacter(user_id);
//        return result ? "注册成功" : "注册失败";
//    }
//
//    @GetMapping("/user/{username}")
//    public User getUserByUsername(@PathVariable String username) {
//        return userService.findByUsername(username);
//    }
//
//    @GetMapping("/character/{user_id}")
//    public Character getCharacterById(@PathVariable Integer user_id) {
//        return characterService.getCharacterById(user_id);
//    }
//
//    @PostMapping("/character/update")
//    public ResponseEntity<String> updateProfile(
//            @RequestParam("userId") Integer userId,
//            @RequestParam("name") String name,
//            @RequestParam("biography") String biography,
//            @RequestParam(value = "file", required = false) MultipartFile file) {
//
//        try {
//            // 创建上传目录
//            File uploadDir = new File(UPLOAD_DIR);
//            System.out.println("UPLOAD_DIR 路径: " + uploadDir.getAbsolutePath());
//            if (!uploadDir.exists()) {
//                uploadDir.mkdirs();  // 如果目录不存在，创建目录
//            }
//            String avatarUrl = null;
//            if (file != null && !file.isEmpty()) {
//                System.out.println("文件名: " + file.getOriginalFilename());
//                System.out.println("文件大小: " + file.getSize());
//                // 生成唯一文件名
//                String originalFilename = file.getOriginalFilename();
//                System.out.println("生成了文件名");
//                String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;
//                System.out.println("生成了唯一文件名");
//                avatarUrl = "images/" + uniqueFilename;  // 保存的路径
//                System.out.println("生成了对应的url");
//                File savedFile = new File(uploadDir, uniqueFilename);
//                System.out.println("创建了对应的对象");
//                file.transferTo(savedFile);  // 保存文件
//                System.out.println("保存了文件");
//            } else {
//                System.out.println("file 对象为空或文件为空");
//            }
//
//            System.out.println("执行了生成url");
//            // 更新用户信息
//            boolean flag = characterService.updateCharacter(userId, name, biography, avatarUrl);
//            if (!flag) {
//                System.out.println("service部分出错了");
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("service部分出错了");
//            }
//            System.out.println("用户信息更新成功");
//            return ResponseEntity.ok("用户信息更新成功");
//        } catch (IOException e) {
//            System.out.println("未执行try部分");
//            e.printStackTrace();  // 打印堆栈信息
//            System.out.println("Exception: " + e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失败");
//        }
//    }
//
//    @GetMapping("/post/{id}")
//    public Post getPostById(@PathVariable Integer id) {
//        return postService.getPostById(id);
//    }
//
//    @PostMapping("/post")
//    public boolean insertPost(@RequestBody Post post) {
//        return postService.insertPost(post);
//    }
////
////    @Autowired
////    private ChatService chatService;
////
////    @PostMapping("/chat")
////    public ResponseEntity<String> chat(@RequestBody ChatRequest request) {
////        String response = chatService.chatWithAI(request.getMessage(), request.getConversationId());
////        return ResponseEntity.ok(response);
////    }
////
////    @GetMapping("/conversations")
////    public ResponseEntity<?> getConversations() {
////        // 实现获取用户对话历史的逻辑
////        return ResponseEntity.ok(chatService.getConversations());
////    }
////
////    @GetMapping("/conversations/{conversationId}/messages")
////    public ResponseEntity<?> getMessages(@PathVariable Long conversationId) {
////        // 实现获取特定对话消息记录的逻辑
////        return ResponseEntity.ok(chatService.getMessages(conversationId));
////    }
//}