package com.example.doumiproject.repository;

import com.example.doumiproject.dto.PostDto;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

public interface PostRepository {

    public List<PostDto> findAllPost(int page, int pageSize, String type);
    public int getTotalPages(int pageSize, String type);
    public int getTotalPagesForSearch(int pageSize, String keyword, String type);
    public List<PostDto> findByTitleOrAuthor(String keyword, String type, int page, int pageSize);
    public List<PostDto> findByTag(String tag, int page, int pageSize);
    public int getTotalPagesForTag(int pageSize, String tag);

    default RowMapper<PostDto> postDtoRowMapper() {
        return ((rs, rowNum) -> {
            PostDto postDto = new PostDto();
            postDto.setId(rs.getLong("id"));
            postDto.setUserId(rs.getString("author"));
            postDto.setTitle(rs.getString("title"));
            postDto.setContents(rs.getString("contents"));
            postDto.setCreatedAt(rs.getTimestamp("created_at"));
            postDto.setLikeCount(rs.getLong("like_count"));
            return postDto;
        });
    };
}
