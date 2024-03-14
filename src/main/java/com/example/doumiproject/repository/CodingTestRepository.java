package com.example.doumiproject.repository;

import com.example.doumiproject.dto.CodingTestDto;
import com.example.doumiproject.dto.CodingTestRequestDto;
import org.springframework.jdbc.core.RowMapper;

import java.util.Optional;

public interface CodingTestRepository {
    public Optional<CodingTestDto> findByCodingTestId(long post_id, long user_id);
    public Long saveCodingTest(CodingTestRequestDto cote, long userId);
    void updateCodingTest(CodingTestRequestDto cote, long postId);
    void deleteCodingTest(long postId);

    default RowMapper<CodingTestDto> codingTestDtoRowMapper() {

        return ((rs, rowNum) -> {
            CodingTestDto codingTestDto = CodingTestDto.builder()
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
            return codingTestDto;
        });
    }
}

