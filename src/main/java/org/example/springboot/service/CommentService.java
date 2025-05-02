package org.example.springboot.service;

import org.example.springboot.mapper.CommentMapper;
import org.example.springboot.mapper.PostMapper;
import org.example.springboot.pojo.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private PostMapper postMapper;

    public boolean insertComment(Integer userId, Integer postId, String content) {
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setPostId(postId);
        comment.setContent(content);
        comment.setCreatedAt(LocalDateTime.now());
        return commentMapper.insertComment(comment) > 0 && postMapper.incrementCommentsCount(comment.getPostId()) > 0;
    }

    public boolean deleteComment(Integer commentId) {
        return commentMapper.deleteComment(commentId) > 0 && postMapper.decrementCommentsCount(commentId) > 0;
    }

    public List<Comment> getUserComments(Integer userId) {
        return commentMapper.getUserComments(userId);
    }

    public List<Comment> getPostComments(Integer postId) {
        return commentMapper.getPostComments(postId);
    }

    public Comment getCommentById(Integer commentId) {
        return commentMapper.getCommentById(commentId);
    }
}