package com.example.doumiproject.repository;

import com.example.doumiproject.dto.PostDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

@Repository
public class JdbcTemplatePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplatePostRepository(DataSource dataSource) {

        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<PostDto> findAllPost(int page, int pageSize, String type) {

        int offset = (page - 1) * pageSize;

        String sql = "select p.id," +
            "u.user_id as author," +
            "p.type, p.title, p.contents, p.created_at," +
            "p.updated_at, " +
            "(select count(*) from likes where post_id = p.id and type = 'POST') as like_count " +
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
    public int getTotalPages(int pageSize, String type) {

        String sql = "select ceil(count(*) / ?) as totalPages " +
            "from post " +
            "where type = ?";

        return jdbcTemplate.queryForObject(sql, Integer.class, pageSize, type);
    }

    @Override
    public List<PostDto> findByTitleOrAuthor(String keyword, String type, int page, int pageSize) {

        String param = "%" + keyword + "%";
        int offset = (page - 1) * pageSize;

        String sql = "select p.id," +
            "u.user_id as author," +
            "p.type, p.title, p.contents, p.created_at," +
            "p.updated_at, " +
            "(select count(*) from likes where post_id = p.id and type = 'POST') as like_count " +
            "from post p " +
            "inner join " +
            "user u on p.user_id = u.id " +
            "where " +
            "(p.type = ?) and" +
            "(p.title like ? or u.user_id like ? )" +
            "order by " +
            "p.id desc " +
            "limit ? offset ?";

        return jdbcTemplate.query(sql, postDtoRowMapper(), type, param, param, pageSize, offset);
    }

    @Override
    public List<PostDto> findByTag(String tag, int page, int pageSize) {

        String type = "QUIZ";
        String sqlSelector = isTagType(tag) ? "t.type = ? " : "t.name = ? ";

        int offset = (page - 1) * pageSize;

        String sql = "select p.id, p.title, p.user_id as author, p.contents, p.created_at, " +
            "(select count(*) from likes where post_id = p.id and type = 'POST') as like_count " +
            "from post p " +
            "left join " +
            "quiztag qt on p.id = qt.post_id " +
            "left join " +
            "tag t on qt.tag_id = t.id " +
            "where " +
            "p.type = ? AND " + sqlSelector +
            "order by p.id desc " +
            "limit ? offset ?";

        return jdbcTemplate.query(sql, postDtoRowMapper(), type, tag, pageSize, offset);
    }

    @Override
    public int getTotalPagesForTag(int pageSize, String tag) {

        String type = "QUIZ";
        String sqlSelector = isTagType(tag) ? "t.type = ? " : "t.name = ? ";

        String sql = "select ceil(count(*) / ?) " +
            "from post p " +
            "left join " +
            "quiztag qt on p.id = qt.post_id " +
            "left join " +
            "tag t on qt.tag_id = t.id " +
            "where " +
            "p.type = ? AND " +
            sqlSelector;

        return jdbcTemplate.queryForObject(sql, Integer.class, pageSize, type, tag);
    }

    private boolean isTagType(String tag) {

        List<String> tagList = Arrays.asList("Java", "Spring", "DB", "AWS", "FrontEnd");
        return tagList.contains(tag);
    }

    @Override
    public int getTotalPagesForSearch(int pageSize, String keyword, String type) {

        String param = "%" + keyword + "%";

        String sql = "select ceil(count(*) / ?) " +
            "from post p " +
            "inner join " +
            "user u on p.user_id = u.id " +
            "where " +
            "(p.type = ?) and " +
            "(p.title like ? or u.user_id like ? )";

        return jdbcTemplate.queryForObject(sql, Integer.class, pageSize, type, param, param);
    }

    @Override
    public List<PostDto> findAllUserCodingTestPosts(Long userId) {
        String sql =
            "SELECT p.id, p.user_id AS author, p.title, p.contents, p.created_at, p.type, COUNT(l.id) AS like_count "
                + "FROM post p "
                + "LEFT JOIN likes l "
                + "ON p.id = l.post_id "
                + "WHERE p.user_id = ? AND p.type = 'COTE' "
                + "GROUP BY p.id "
                + "ORDER BY p.created_at DESC";
        return jdbcTemplate.query(sql, postDtoRowMapper(), userId);
    }

    @Override
    public List<PostDto> findAllUserQuizPosts(Long userId, int page, int pageSize) {

        int offset = (page - 1) * pageSize;

        String sql =
            "SELECT p.id, p.user_id AS author, p.title, p.contents, p.created_at, p.type, COUNT(l.id) AS like_count "
                + "FROM post p "
                + "LEFT JOIN likes l "
                + "ON p.id = l.post_id "
                + "WHERE p.user_id = ? AND p.type = 'QUIZ' "
                + "GROUP BY p.id "
                + "ORDER BY p.created_at DESC "
                + "limit ? offset ?";
        return jdbcTemplate.query(sql, postDtoRowMapper(), userId, pageSize, offset);
    }

    @Override
    public int getTotalPagesForMyPage(Long userId, String type, int pageSize) {

        String sql = "select ceil(count(*) / ?) " +
                "from post p " +
                "inner join " +
                "user u on p.user_id = u.id " +
                "where " +
                "(p.type = ?) and (u.id = ?)";

        return jdbcTemplate.queryForObject(sql, Integer.class, pageSize, type, userId);
    }
}
