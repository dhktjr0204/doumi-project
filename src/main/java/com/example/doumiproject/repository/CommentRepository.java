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
            CommentDto commentDto = new CommentDto();
            commentDto.setId(rs.getLong("comment_id"));
            commentDto.setUserId(rs.getLong("user_id"));
            commentDto.setAuthor(rs.getString("author"));
            commentDto.setType(rs.getString("type"));
            commentDto.setContents(rs.getString("contents"));
            commentDto.setDisplay(rs.getInt("display"));
            commentDto.setCreatedAt(rs.getTimestamp("created_at"));
            commentDto.setLikeCount(rs.getLong("like_count"));
            commentDto.setLiked(rs.getString("is_liked").equals("Y") ? true : false);

            return commentDto;
        };
    }

    default RowMapper<ReCommentDto> reCommentRowMapper(){
        return (rs,rowNum)->{
            ReCommentDto reCommentDto = new ReCommentDto();
            reCommentDto.setId(rs.getLong("re_comment_id"));
            reCommentDto.setUserId(rs.getLong("user_id"));
            reCommentDto.setAuthor(rs.getString("author"));
            reCommentDto.setType(rs.getString("type"));
            reCommentDto.setContents(rs.getString("contents"));
            reCommentDto.setDisplay(rs.getInt("display"));
            reCommentDto.setCreatedAt(rs.getTimestamp("created_at"));
            reCommentDto.setLikeCount(rs.getLong("like_count"));
            reCommentDto.setLiked(rs.getString("is_liked").equals("Y") ? true : false);

            return reCommentDto;
        };
    }
}
