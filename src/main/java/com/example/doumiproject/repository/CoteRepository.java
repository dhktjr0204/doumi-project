package com.example.doumiproject.repository;

import com.example.doumiproject.dto.CoteDto;
import com.example.doumiproject.dto.CoteRequestDto;
import org.springframework.jdbc.core.RowMapper;

import java.util.Optional;

public interface CoteRepository {
    public Optional<CoteDto> findByCoteId(long post_id, long user_id);
    public Long saveCote(CoteRequestDto cote, long userId);
    void updateCote(CoteRequestDto cote, long postId);
    void deleteCote(long postId);

    default RowMapper<CoteDto> coteDtoRowMapper() {

        return ((rs, rowNum) -> {
            CoteDto coteDto= CoteDto.builder()
                    .id(rs.getLong("post_id"))
                    .userId(rs.getLong("user_id"))
                    .author(rs.getString("author"))
                    .title(rs.getString("title"))
                    .contents(rs.getString("contents"))
                    .postType(rs.getString("post_type"))
                    .createdAt(rs.getTimestamp("created_at"))
                    .updatedAt(rs.getTimestamp("updated_at"))
                    .likeCount(rs.getLong("like_count"))
                    .isLiked(rs.getString("is_liked").equals("Y") ? true : false).build();
            return coteDto;
        });
    }
}

