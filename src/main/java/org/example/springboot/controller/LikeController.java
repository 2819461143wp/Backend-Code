package org.example.springboot.controller;

import org.example.springboot.service.LikeService;
import org.example.springboot.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @GetMapping("incrementlike")
    public ResponseEntity<String> likePost(@RequestParam("userId") Integer userId,@RequestParam("postId") Integer postId) {
        // 处理点赞逻辑
        boolean success = likeService.incrementLikesCount(userId,postId);
        if (success) {
            return ResponseEntity.ok("点赞成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("点赞失败");
        }
    }

    @GetMapping("decrementlike")
    public ResponseEntity<String> unlikePost(@RequestParam("userId") Integer userId,@RequestParam("postId") Integer postId) {
        // 处理取消点赞逻辑
        boolean success = likeService.decrementLikesCount(userId,postId);
        if (success) {
            return ResponseEntity.ok("取消点赞成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("取消点赞失败");
        }
    }

    @GetMapping("getUserLikes")
    public ResponseEntity<List<Integer>> getUserLikes(
            @RequestParam("userId") Integer userId,
            @RequestParam("postIds") String postIdsStr) {

        // 将字符串转为整数列表
        List<Integer> postIds = Arrays.stream(postIdsStr.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // 获取用户已点赞的帖子ID列表
        List<Integer> likedPostIds = likeService.getUserLikedPostIds(userId, postIds);

        return ResponseEntity.ok(likedPostIds);
    }
}
