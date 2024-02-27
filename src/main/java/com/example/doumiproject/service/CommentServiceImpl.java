package com.example.doumiproject.service;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;

    @Override
    public void saveComment(Comment comment, long userId, String type) {

        commentRepository.saveComment(comment, userId, type);
    }

    @Override
    public List<CommentDto> getAllComments(long postId) {

        return commentRepository.getAllComment(postId);
    }

    @Override
    public CommentDto getComment(long commentId) {
        return commentRepository.getComment(commentId);
    }
}
