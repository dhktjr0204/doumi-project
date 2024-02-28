package com.example.doumiproject.repository;

import com.example.doumiproject.dto.PostDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class JdbcTemplatePostRepository implements PostRepository{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplatePostRepository(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<PostDto> findAllQuiz(int page, int pageSize) {

        int offset = (page - 1) * pageSize;
        String type = "QUIZ";

        String sql = "select p.id," +
                "u.user_id as author," +
                "p.type, p.title, p.contents, p.created_at," +
                "p.updated_at, p.like as like_count " +
                "from post p " +
                "inner join " +
                "user u on p.user_id = u.id " +
                "where p.type = ? " +
                "order by " +
                "p.id desc " +
                "limit ? offset ?";

        return jdbcTemplate.query(sql, postDtoRowMapper(), type, pageSize, offset);
    }

    @Override
    public List<PostDto> findAllQuiz() {

        String type = "QUIZ";

        String sql = "select p.id," +
                "u.user_id as author," +
                "p.type, p.title, p.contents, p.created_at," +
                "p.updated_at, p.like as like_count " +
                "from post p " +
                "inner join " +
                "user u on p.user_id = u.id " +
                "where p.type = ? " +
                "order by " +
                "p.id desc ";

        return jdbcTemplate.query(sql, postDtoRowMapper(), type);
    }

    @Override
    public int getTotalPages(int pageSize) {

        String type = "QUIZ";

        String sql = "select ceil(count(*) / ?) as totalPages " +
                "from post " +
                "where type = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, pageSize, type);
    }

    @Override
    public List<PostDto> findByTitleOrAuthor(String keyword, int page, int pageSize) {

        String param = "%"+keyword+"%";
        String type = "QUIZ";
        int offset = (page - 1) * pageSize;

        String sql = "select p.id," +
                "u.user_id as author," +
                "p.type, p.title, p.contents, p.created_at," +
                "p.updated_at, p.like as like_count " +
                "from post p " +
                "inner join " +
                "user u on p.user_id = u.id " +
                "where " +
                "(p.type = ?) " +
                "(p.title like ? or u.user_id like ? )" +
                "order by " +
                "p.id desc "+
                "limit ? offset ?";

        return jdbcTemplate.query(sql, postDtoRowMapper(), type, param, param, pageSize, offset);
    }

    @Override
    public List<PostDto> findByTag(String tag, int page, int pageSize) {

        String type = "QUIZ";
        int offset = (page - 1) * pageSize;

        String sql = "select p.id, p.title, p.user_id as author, p.contents, p.created_at " +
                "from post p " +
                "left join " +
                "quiztag qt on p.id = qt.post_id " +
                "left join " +
                "tag t on qt.tag_id = t.id " +
                "where " +
                "p.type = ? AND " +
                "t.name = ? " +
                "order by p.id desc " +
                "limit ? offset ?";

        return jdbcTemplate.query(sql, postDtoRowMapper(), type, tag, pageSize, offset);
    }

    @Override
    public int getTotalPagesForTag(int pageSize, String tag) {

        String type = "QUIZ";

        String sql = "select ceil(count(*) / ?) " +
                "from post p " +
                "left join " +
                "quiztag qt on p.id = qt.post_id " +
                "left join " +
                "tag t on qt.tag_id = t.id " +
                "where " +
                "p.type = ? AND " +
                "t.name = ? ";

        return jdbcTemplate.queryForObject(sql, Integer.class, pageSize, type, tag);
    }

    @Override
    public int getTotalPages(int pageSize, String keyword) {

        String param = "%"+keyword+"%";
        String type = "QUIZ";

        String sql = "select ceil(count(*) / ?) " +
                "from post p " +
                "inner join " +
                "user u on p.user_id = u.id " +
                "where " +
                "(p.type = ? )" +
                "(p.title like ? or u.user_id like ? )";

        return jdbcTemplate.queryForObject(sql, Integer.class, pageSize, type, param, param);
    }

    @Override
    public List<PostDto> findAllPostWithType(int page, int pageSize, String type) {

        int offset = (page - 1) * pageSize;

        String sql = "select p.id," +
                "u.user_id as author," +
                "p.type, p.title, p.contents, p.created_at," +
                "p.updated_at, p.like as like_count " +
                "from post p " +
                "inner join " +
                "user u on p.user_id = u.id " +
                "where type = ? " +
                "order by " +
                "p.id desc " +
                "limit ? offset ?";

        return jdbcTemplate.query(sql, postDtoRowMapper(), type, pageSize, offset);
    }

    @Override
    public List<PostDto> findAllPostWithType(String type) {

        String sql = "select p.id," +
                "u.user_id as author," +
                "p.type, p.title, p.contents, p.created_at," +
                "p.updated_at, p.like as like_count " +
                "from post p " +
                "inner join " +
                "user u on p.user_id = u.id " +
                "where type = ? " +
                "order by " +
                "p.id desc ";

        return jdbcTemplate.query(sql, postDtoRowMapper(), type);
    }
}
