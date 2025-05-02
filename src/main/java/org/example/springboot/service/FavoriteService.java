package org.example.springboot.service;

import org.example.springboot.mapper.FavoriteMapper;
import org.example.springboot.mapper.PostMapper;
import org.example.springboot.pojo.Favorite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteMapper favoriteMapper;

    @Autowired
    private PostMapper postMapper;

    public boolean incrementFavoritesCount(Integer userId, Integer postId) {
        Favorite favorite = new Favorite();
        favorite.setUserId(userId);
        favorite.setPostId(postId);
        return favoriteMapper.insertFavorite(favorite) > 0 && postMapper.incrementFavoritesCount(postId) > 0;
    }

    public boolean decrementFavoritesCount(Integer userId, Integer postId) {
        return favoriteMapper.deleteFavorite(userId, postId) > 0 && postMapper.decrementFavoritesCount(postId) > 0;
    }

    public List<Integer> getUserFavoritePostIds(Integer userId, List<Integer> postIds) {
        return favoriteMapper.getUserFavoritePostIds(userId, postIds);
    }
}
