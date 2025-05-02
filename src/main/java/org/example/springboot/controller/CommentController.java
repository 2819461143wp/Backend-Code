package org.example.springboot.controller;

import org.example.springboot.pojo.Comment;
import org.example.springboot.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/insert")
    public ResponseEntity<String> insertComment(@RequestParam("userId") Integer userId, @RequestParam("postId") Integer postId, @RequestParam("content") String content) {
        // 处理添加评论逻辑
        boolean success = commentService.insertComment(userId, postId, content);
        if (success) {
            return ResponseEntity.ok("评论成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("评论失败");
        }
    }



    @GetMapping("/getPostComments")
    public ResponseEntity<List<Comment>> getPostComments(@RequestParam("postId") Integer postId) {
        // 获取帖子的所有评论
        List<Comment> comments = commentService.getPostComments(postId);
        return ResponseEntity.ok(comments);
    }
}

//@GetMapping("/delete")
//public ResponseEntity<String> deleteComment(@RequestParam("commentId") Integer commentId) {
//    // 处理删除评论逻辑
//    boolean success = commentService.deleteComment(commentId);
//    if (success) {
//        return ResponseEntity.ok("删除评论成功");
//    } else {
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("删除评论失败");
//    }
//}
//
//@GetMapping("/getUserComments")
//public ResponseEntity<List<Comment>> getUserComments(@RequestParam("userId") Integer userId) {
//    // 获取用户的所有评论
//    List<Comment> comments = commentService.getUserComments(userId);
//    return ResponseEntity.ok(comments);
//}