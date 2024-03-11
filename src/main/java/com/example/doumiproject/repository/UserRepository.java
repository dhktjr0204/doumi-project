package com.example.doumiproject.repository;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.entity.User;

import java.util.List;
import org.springframework.jdbc.core.RowMapper;

public interface UserRepository {

    User save(User user); //회원을 저장하면 회원이 반환되는 기능

    User findByUserId(String userId); //userId로 회원을 찾는 기능

    List<User> findAllUser(); //모든 회원 리스트를 반환하는 기능

    List<PostDto> findAllUserCodingTestPosts(long userId);

    List<PostDto> findAllUserQuizPosts(long userId);

    List<CommentDto> findAllUserCommentPosts(long userId);

    default RowMapper<User> userRowMapper() {
        return ((rs, rowNum) -> {
            User user = User.builder()
                .id(rs.getLong("id"))
                .userId(rs.getString("user_id"))
                .password(rs.getString("password"))
                .build();

            return user;
        });
    }
}
