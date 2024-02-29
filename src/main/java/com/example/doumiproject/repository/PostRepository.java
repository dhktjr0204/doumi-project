package com.example.doumiproject.repository;

import com.example.doumiproject.dto.PostDto;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface PostRepository {

    public List<PostDto> findAllQuiz(int page, int pageSize);
    public List<PostDto> findAllQuiz();
    public int getTotalPages(int pageSize);
    public int getTotalPages(int pageSize, String keyword);
    public List<PostDto> findByTitleOrAuthor(String keyword, int page, int pageSize);
    public List<PostDto> findByTag(String tag, int page, int pageSize);
    public int getTotalPagesForTag(int pageSize, String tag);
    List<PostDto> findAllPostWithType(int page, int pageSize, String type);
    List<PostDto> findAllPostWithType(String type);
    default RowMapper<PostDto> postDtoRowMapper() {
        return ((rs, rowNum) -> {
            PostDto postDto = new PostDto();
            postDto.setId(rs.getLong("id"));
            postDto.setUserId(rs.getString("author"));
            postDto.setTitle(rs.getString("title"));
            postDto.setContents(rs.getString("contents"));
            postDto.setCreatedAt(rs.getTimestamp("created_at"));
            return postDto;
        });
    };
}
