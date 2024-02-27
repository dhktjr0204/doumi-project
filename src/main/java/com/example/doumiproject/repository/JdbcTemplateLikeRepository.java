package com.example.doumiproject.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class JdbcTemplateLikeRepository implements LikeRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateLikeRepository(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void addLike(long user_id, long post_id) {

        String sql = "insert into likes(user_id, post_id) " +
                "values (?, ?)";

        jdbcTemplate.update(sql, user_id, post_id);
    }

    @Override
    public void cancelLike(long user_id, long post_id) {

        String sql = "delete from likes " +
                "where user_id = ? and post_id = ?";

        jdbcTemplate.update(sql, user_id, post_id);
    }

    @Override
    public long countLike(long post_id) {

        String sql = "select count(*) " +
                "from likes " +
                "where post_id = ?";

        return jdbcTemplate.queryForObject(sql, Long.class, post_id);
    }
}
