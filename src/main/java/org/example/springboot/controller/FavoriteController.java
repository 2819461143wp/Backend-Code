package org.example.springboot.controller;

import org.example.springboot.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @GetMapping("/increment")
    public String incrementLike(Integer userId, Integer postId) {
        // 处理收藏逻辑
        boolean success = favoriteService.incrementFavoritesCount(userId, postId);
        if (success) {
            return "收藏成功";
        } else {
            return "收藏失败";
        }
    }
    @GetMapping("/decrement")
    public String decrementLike(Integer userId, Integer postId) {
        // 处理取消收藏逻辑
        boolean success = favoriteService.decrementFavoritesCount(userId, postId);
        if (success) {
            return "取消收藏成功";
        } else {
            return "取消收藏失败";
        }
    }

    @GetMapping("/getUserFavorites")
    public ResponseEntity<List<Integer>> getUserFavorites(
            @RequestParam("userId") Integer userId,
            @RequestParam("postIds") String postIdsStr) {
        // 将字符串转为整数列表
        List<Integer> postIds = List.of(postIdsStr.split(",")).stream()
                .map(Integer::parseInt)
                .toList();

        // 获取用户已收藏的帖子ID列表
        List<Integer> likedPostIds = favoriteService.getUserFavoritePostIds(userId, postIds);

        return ResponseEntity.ok(likedPostIds);
    }
}
