package org.example.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.springboot.pojo.Comment;

import java.util.List;

@Mapper
public interface CommentMapper {
    // 添加评论
    int insertComment(Comment comment);

    // 删除评论
    int deleteComment(Integer commentId);

    // 获取用户的所有评论
    List<Comment> getUserComments(Integer userId);

    // 获取帖子的所有评论
    List<Comment> getPostComments(Integer postId);

    // 获取单个评论
    Comment getCommentById(Integer commentId);
}
