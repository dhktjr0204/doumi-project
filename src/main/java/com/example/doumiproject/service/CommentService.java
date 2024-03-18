package com.example.doumiproject.service;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.entity.Comment;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface CommentService {
    void saveComment(Comment comment, long userId);
    List<CommentDto> getAllComments(long postId, long userId);
    List<CommentDto> getAllCommentsOrderByLikeCount(long postId, long userId);
    void updateComment(Comment comment, long commentId);
    void deleteComment(long commentId);
    int getTotalPagesForMyPage(long userId, int pageSize);
    List<CommentDto> getCommentList(long userId, int page, int pageSize);
}
