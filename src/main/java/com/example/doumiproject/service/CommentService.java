package com.example.doumiproject.service;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.entity.Comment;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface CommentService {
    void saveComment(Comment comment, long userId);
    public List<CommentDto> getAllComments(long postId, long userId);
    public List<CommentDto> getAllCommentsOrderByLikeCount(long postId, long userId);
    void updateComment(Comment comment, long commentId);
    void deleteComment(long commentId);
}
