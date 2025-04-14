package org.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.springboot.pojo.Post;

import java.util.List;

@Mapper
public interface PostMapper {
    Post getPostById(Integer id);
    int insertPost(Post post);
    int updatePost(Post post);

    // 获取所有帖子
    List<Post> getAllPosts();
    List<Post> getPosts(Integer status);

    List<Post> adminGetAllPosts(@Param("title") String title,
                           @Param("status") Integer status,
                           @Param("allow") Integer allow);
    int updatePostAllow(@Param("id") Integer id, @Param("allow") Integer allow);
    int deletePost(@Param("id") Integer id);
}