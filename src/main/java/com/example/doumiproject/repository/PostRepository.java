package com.example.doumiproject.repository;

import com.example.doumiproject.dto.PostDto;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface PostRepository {

    List<PostDto> findAllPost(int page, int pageSize, String type);

    int getTotalPages(int pageSize, String type);

    int getTotalPagesForSearch(int pageSize, String keyword, String type);

    List<PostDto> findByTitleOrAuthor(String keyword, String type, int page, int pageSize);

    List<PostDto> findByTag(String tag, int page, int pageSize);

    int getTotalPagesForTag(int pageSize, String tag);

    List<PostDto> findAllUserCodingTestPosts(Long userId);

    List<PostDto> findAllUserQuizPosts(Long userId, int page, int pageSize);
    int getTotalPagesForMyPage(Long userId, String type, int pageSize);

    default RowMapper<PostDto> postDtoRowMapper() {
        return ((rs, rowNum) -> {

            PostDto postDto = PostDto.builder()
                .id(rs.getLong("id"))
                .userId(rs.getString("author"))
                .title(rs.getString("title"))
                .contents(rs.getString("contents"))
                .createdAt(rs.getTimestamp("created_at"))
                .likeCount(rs.getLong("like_count"))
                .build();

            return postDto;
        });
    }
}
