package com.example.doumiproject.repository;

import com.example.doumiproject.dto.*;
import com.example.doumiproject.entity.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcTemplateCommentRepository implements CommentRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<CommentDto> getAllComment(long postId) {
        String sql = "select c.id as comment_id ,u.id as user_id, u.user_id as author , c.contents, c.created_at, c.like, c.display " +
                "from comment c " +
                "inner join user u on u.id=c.user_id " +
                "where c.post_id=? " +
                "and c.parent_comment_id=0 " +
                "order by c.created_at DESC ";

        List<CommentDto> comments = jdbcTemplate.query(sql, commentRowMapper(), postId);

        for (CommentDto comment : comments) {
            List<ReCommentDto> reComments = getAllReComment(comment.getId());
            comment.setReComments(reComments);
        }

        return comments;
    }


    @Override
    public List<ReCommentDto> getAllReComment(long parentCommentId) {
        // 1. 부모 댓글 ID에 해당하는 모든 대댓글 목록을 조회
        //대댓글 시간순 정렬
        String sql = "select c.id as re_comment_id ,u.id as user_id, u.user_id as author, c.contents, c.created_at, c.like, c.display " +
                "from comment c " +
                "inner join user u on u.id=c.user_id " +
                "where c.parent_comment_id=? " +
                "order by c.created_at ASC";

        return jdbcTemplate.query(sql, reCommentRowMapper(), parentCommentId);
    }

    @Override
    public void saveComment(Comment comment, long userId, String type) {

        String sql = "insert into comment(user_id, post_id, type, contents, created_at, updated_at, `like`, display, parent_comment_id) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

        //0이면 공개
        int display=0;
        //체크 박스 선택한 상태이면
        if (comment.isDisplay()) {
            //1로 바꿔줌(비공개 설정)
            display = 1;
        }

        jdbcTemplate.update(sql, userId, comment.getPostId(), type, comment.getContents(),
                LocalDateTime.now(),LocalDateTime.now(),0,display,comment.getParentCommentId());
    }

    @Override
    public CommentDto getComment(long commentId) {
        String sql = "select c.id as comment_id ,u.id as user_id, u.user_id as author , c.contents, c.created_at, c.like, c.display " +
                "from comment c " +
                "inner join user u on u.id=c.user_id " +
                "where c.id=? ";

        return jdbcTemplate.queryForObject(sql, commentRowMapper(), commentId);
    }
}
