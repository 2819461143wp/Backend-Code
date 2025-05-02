package org.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.springboot.pojo.Favorite;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    int insertFavorite(Favorite favorite);
    int deleteFavorite(Integer userId, Integer postId);
    List<Integer> getUserFavoritePostIds(Integer userId,List<Integer> postIds);
    List<Integer> getuserFavorite(Integer userId);
}
