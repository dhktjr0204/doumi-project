package com.example.doumiproject.service;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.entity.Comment;

import java.util.List;

public interface CommentService {
    void saveComment(Comment comment, long userId, String type);
    public List<CommentDto> getAllComments(long postId);
    public CommentDto getComment(long commentId);
}
