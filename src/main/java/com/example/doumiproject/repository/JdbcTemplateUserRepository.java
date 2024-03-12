package com.example.doumiproject.repository;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.entity.User;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateUserRepository implements UserRepository {

    //Spring에서 JdbcTemplate를 사용하면 JDBC를 편리하게 사용할 수 있다
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User save(User user) {
        //SQL 쿼리 준비
        String sql = "INSERT INTO user(user_id,password,role)"
            + " VALUES(?,?,?)";
        //insert한 행에 생성된 id를 반환받기 위해 GeneratedKeyHolder를 사용한다
        KeyHolder keyHolder = new GeneratedKeyHolder();

        //JdbcTemplate의 update 메소드를 사용해 SQL쿼리를 실행한다
        //update 메소드는 PreparedStatementCreator와 KeyHolder를 매개변수로 받아 실행되고, PreparedStatement에서 사용할 값을 설정한다
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql,
                Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getUserId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getRole());
            return ps;
        }, keyHolder);

        //DB에서 생성된 id를 User 객체에 설정한다
        long userId = keyHolder.getKey().longValue();
        user.setId(userId);

        return user;//이걸 로그로 찍어봐야하나?
    }

    @Override
    public User findByUserId(String userId) {
        String sql = "SELECT id,user_id,password FROM user WHERE user_id = ?";

        List<User> users = jdbcTemplate.query(sql, userRowMapper(), userId);

        return users.isEmpty() ? null : users.get(0);
    }

    @Override
    public List<User> findAllUser() {
        return null;
    }

    @Override
    public List<PostDto> findAllUserCodingTestPosts(long userId) {
        String sql = "SELECT * FROM post WHERE user_id = ? AND (type = 'COTE')";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            return PostDto.builder()
                .id(rs.getLong("id"))
                .userId(rs.getString("user_id"))
                .type(rs.getString("type"))
                .title(rs.getString("title"))
                .contents(rs.getString("contents"))
                .createdAt(rs.getTimestamp("created_at"))
                .updatedAt(rs.getTimestamp("updated_at"))
//                .likeCount(rs.getLong("likeCount")) ?????
                .build();
        });
    }

    @Override
    public List<PostDto> findAllUserQuizPosts(long userId) {
        String sql = "SELECT * FROM post WHERE user_id = ? AND (type = 'QUIZ')";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            return PostDto.builder()
                .id(rs.getLong("id"))
                .userId(rs.getString("user_id"))
                .type(rs.getString("type"))
                .title(rs.getString("title"))
                .contents(rs.getString("contents"))
                .createdAt(rs.getTimestamp("created_at"))
                .updatedAt(rs.getTimestamp("updated_at"))
//                .likeCount(rs.getLong("likeCount")) ?????
                .build();
        });
    }

    //수정
    @Override
    public List<CommentDto> findAllUserCommentPosts(long userId) {
        String sql = "SELECT user_id,post_id,contents,updated_at FROM comment WHERE user_id = ?";

        return jdbcTemplate.query(sql, new Object[]{userId}, (rs, rowNum) -> {
            return CommentDto.builder().build();
        });
    }

}
