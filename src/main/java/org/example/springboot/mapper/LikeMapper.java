package org.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.springboot.pojo.Like;

import java.util.List;

@Mapper
public interface LikeMapper {
    int insertLike(Like like);
    int deleteLike(@Param("userId") Integer userId, @Param("postId") Integer postId);
    int countLikesByPostId(Integer postId);
    boolean existsLike(@Param("userId") Integer userId, @Param("postId") Integer postId);
    List<Integer> getUserLikedPostIds(@Param("userId") Integer userId, @Param("postIds") List<Integer> postIds);
}
