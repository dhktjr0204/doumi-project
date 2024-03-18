package com.example.doumiproject.repository;

import com.example.doumiproject.dto.*;
import com.example.doumiproject.entity.Comment;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface CommentRepository {
    List<CommentDto> getAllComment(long postId, long userId);
    List<CommentDto> getAllCommentOrderByLikeCount(long postId, long userId);
    List<ReCommentDto> getAllReComment(long parentCommentId, long userId);
    void saveComment(Comment comment, long userId);
    void updateComment(Comment comment, long commentId);
    void deleteComment(long commentId);
    List<CommentDto> findByUserId(long userId, int page, int pageSize);
    int getTotalPagesForMyPage(long userId, int pageSize);

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

    default RowMapper<CommentDto> makeMyPageCommentList() {
        return (rs, rowNum) -> {

            CommentDto commentDto = CommentDto.builder()
                    .userId(rs.getLong("user_id"))
                    .postId(rs.getLong("post_id"))
                    .type(rs.getString("type"))
                    .contents(rs.getString("contents"))
                    .updatedAt(rs.getTimestamp("updated_at"))
                    .likeCount(rs.getLong("like_count"))
                    .build();

            return commentDto;
        };
    }
}
