package com.example.doumiproject.service;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.ReCommentDto;
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
    public void saveComment(Comment comment, long userId) {
        Comment newComment = new Comment(
                comment.getUserId(),
                comment.getPostId(),
                comment.getType(),
                comment.getContents(),
                comment.isDisplay(),
                comment.getParentCommentId());

        commentRepository.saveComment(newComment, userId);
    }

    @Override
    public List<CommentDto> getAllComments(long postId,long userId) {
        List<CommentDto> comments = commentRepository.getAllComment(postId, userId);
        for (CommentDto comment : comments) {
            List<ReCommentDto> reComments = commentRepository.getAllReComment(comment.getId(),userId);
            comment.setReComments(reComments);
        }
        return comments;
    }

    @Override
    public List<CommentDto> getAllCommentsOrderByLikeCount(long postId, long userId) {
        List<CommentDto> comments = commentRepository.getAllCommentOrderByLikeCount(postId, userId);
        for (CommentDto comment : comments) {
            List<ReCommentDto> reComments = commentRepository.getAllReComment(comment.getId(),userId);
            comment.setReComments(reComments);
        }
        return comments;
    }

    @Override
    public void updateComment(Comment comment, long commentId) {
        Comment newComment = new Comment(
                comment.getUserId(),
                comment.getPostId(),
                comment.getType(),
                comment.getContents(),
                comment.isDisplay(),
                comment.getParentCommentId());

        commentRepository.updateComment(newComment,commentId);

    }

    @Override
    public void deleteComment(long commentId) {

        commentRepository.deleteComment(commentId);

    }


}
