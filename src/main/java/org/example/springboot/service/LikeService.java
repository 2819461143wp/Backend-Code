package org.example.springboot.service;


import org.example.springboot.mapper.LikeMapper;
import org.example.springboot.mapper.PostMapper;
import org.example.springboot.pojo.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class LikeService {
    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private PostMapper postMapper;

    public boolean incrementLikesCount(Integer userId,Integer postId) {
        Like like = new Like();
        like.setUserId(userId);
        like.setPostId(postId);
        return likeMapper.insertLike(like) > 0 && postMapper.incrementLikesCount(postId) > 0;
    }
    public boolean decrementLikesCount(Integer userId,Integer postId) {
        return likeMapper.deleteLike(userId, postId) > 0 && postMapper.decrementLikesCount(postId) > 0;
    }

    public List<Integer> getUserLikedPostIds(Integer userId, List<Integer> postIds) {
        return likeMapper.getUserLikedPostIds(userId, postIds);
    }
}
