package com.example.doumiproject.repository;

import com.example.doumiproject.dto.*;
import com.example.doumiproject.entity.Comment;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface CommentRepository {
    public List<CommentDto> getAllComment(long postId, long userId);
    public List<CommentDto> getAllCommentOrderByLikeCount(long postId, long userId);
    public List<ReCommentDto> getAllReComment(long parentCommentId, long userId);
    public void saveComment(Comment comment, long userId);
    public void updateComment(Comment comment, long commentId);
    public void deleteComment(long commentId);

    default RowMapper<CommentDto> commentRowMapper(){
        return (rs,rowNum)->{
            CommentDto commentDto = CommentDto.builder()
                    .id(rs.getLong("comment_id"))
                    .userId(rs.getLong("user_id"))
                    .author(rs.getString("author"))
                    .type(rs.getString("type"))
                    .contents(rs.getString("contents"))
                    .display(rs.getInt("display"))
                    .createdAt(rs.getTimestamp("created_at"))
                    .likeCount(rs.getLong("like_count"))
                    .isLiked(rs.getString("is_liked").equals("Y") ? true : false).build();

            return commentDto;
        };
    }

    default RowMapper<ReCommentDto> reCommentRowMapper(){
        return (rs,rowNum)->{
            ReCommentDto reCommentDto = ReCommentDto.builder()
                    .id(rs.getLong("re_comment_id"))
                    .userId(rs.getLong("user_id"))
                    .author(rs.getString("author"))
                    .type(rs.getString("type"))
                    .contents(rs.getString("contents"))
                    .display(rs.getInt("display"))
                    .createdAt(rs.getTimestamp("created_at"))
                    .likeCount(rs.getLong("like_count"))
                    .isLiked(rs.getString("is_liked").equals("Y") ? true : false).build();

            return reCommentDto;
        };
    }
}
