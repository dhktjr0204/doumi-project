package com.example.doumiproject.repository;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.ReCommentDto;
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
    public List<CommentDto> getAllComment(long postId,long userId) {
        String sql = "select c.id as comment_id ,u.id as user_id, u.user_id as author , c.contents, c.created_at, c.type, c.display, " +
                "(select count(*) from likes l where post_id = c.id and l.type = 'COMMENT') as like_count, " +
                "case when exists (select 1 from likes l where post_id = c.id and user_id = ? and l.type = 'COMMENT') then 'Y' else 'N' end as is_liked " +
                "from comment c " +
                "inner join user u on u.id = c.user_id " +
                "where c.post_id = ? " +
                "and c.parent_comment_id = 0 " +
                "order by c.created_at DESC ";

        return jdbcTemplate.query(sql, commentRowMapper(), userId, postId);
    }

    @Override
    public List<CommentDto> getAllCommentOrderByLikeCount(long postId, long userId) {
        String sql="select c.id as comment_id ,u.id as user_id, u.user_id as author , c.contents, c.created_at, c.type, c.display, " +
                "(select count(*) from likes l where post_id = c.id and l.type = 'COMMENT') as like_count, " +
                "case when exists (select 1 from likes l where post_id = c.id and user_id = ? and l.type = 'COMMENT') then 'Y' else 'N' end as is_liked " +
                "from comment c " +
                "inner join user u on u.id = c.user_id " +
                "where c.post_id = ? " +
                "and c.parent_comment_id = 0 " +
                "order by like_count DESC, c.created_at DESC";
        return jdbcTemplate.query(sql, commentRowMapper(), userId, postId);
    }


    @Override
    public List<ReCommentDto> getAllReComment(long parentCommentId, long userId) {
        // 1. 부모 댓글 ID에 해당하는 모든 대댓글 목록을 조회
        //대댓글 시간순 정렬
        String sql = "select c.id as re_comment_id ,u.id as user_id, u.user_id as author, c.contents, c.created_at, c.type, c.display, " +
                "(select count(*) from likes l where post_id = c.id and l.type = 'COMMENT') as like_count, " +
                "case when exists (select 1 from likes l where post_id = c.id and user_id = ? and l.type = 'COMMENT') then 'Y' else 'N' end as is_liked " +
                "from comment c " +
                "inner join user u on u.id = c.user_id " +
                "where c.parent_comment_id = ? " +
                "order by c.created_at ASC";


        return jdbcTemplate.query(sql, reCommentRowMapper(), userId, parentCommentId);
    }

    @Override
    public void saveComment(Comment comment, long userId) {

        String sql = "insert into comment(user_id, post_id, type, contents, created_at, updated_at, display, parent_comment_id) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?) ";

        //0이면 공개
        int display = 0;
        //체크 박스 선택한 상태이면
        if (comment.isDisplay()) {
            //1로 바꿔줌(비공개 설정)
            display = 1;
        }

        jdbcTemplate.update(sql, userId, comment.getPostId(), comment.getType(), comment.getContents(),
                LocalDateTime.now(), LocalDateTime.now(), display, comment.getParentCommentId());
    }

    @Override
    public void updateComment(Comment comment, long commentId) {
        String sql = "update comment " +
                "set contents=?, display=?,updated_at = ? " +
                "where id=?";

        int display = 0;
        if (comment.isDisplay()) {
            display = 1;
        }

        jdbcTemplate.update(sql, comment.getContents(), display,LocalDateTime.now(), commentId);
    }

    @Override
    public void deleteComment(long commentId) {
        String sql="delete " +
                "from comment " +
                "where id=? or parent_comment_id=?";

        jdbcTemplate.update(sql, commentId, commentId);
    }
}
