package com.example.doumiproject.repository;

import com.example.doumiproject.dto.CoteDto;
import com.example.doumiproject.dto.CoteRequestDto;
import org.springframework.jdbc.core.RowMapper;

public interface CoteRepository {
    public CoteDto getByCoteId(long post_id, long user_id);
    public Long saveCote(CoteRequestDto cote, long userId);
    void updateCote(CoteRequestDto cote, long postId);
    void deleteCote(long postId);


    default RowMapper<CoteDto> coteDtoRowMapper() {

        return ((rs, rowNum) -> {
            CoteDto coteDto=new CoteDto();
            coteDto.setId(rs.getLong("post_id"));
            coteDto.setUserId(rs.getLong("user_id"));
            coteDto.setAuthor(rs.getString("author"));
            coteDto.setTitle(rs.getString("title"));
            coteDto.setContents(rs.getString("contents"));
            coteDto.setPostType(rs.getString("post_type"));
            coteDto.setCreatedAt(rs.getTimestamp("created_at"));
            coteDto.setUpdatedAt(rs.getTimestamp("updated_at"));
            coteDto.setLikeCount(rs.getLong("like_count"));
            coteDto.setLiked(rs.getString("is_liked").equals("Y") ? true : false);

            return coteDto;
        });
    }
}

