package com.example.doumiproject.repository;

import com.example.doumiproject.dto.CoteDto;
import com.example.doumiproject.dto.TagDetailDto;
import com.example.doumiproject.entity.Cote;
import org.springframework.jdbc.core.RowMapper;

public interface CoteRepository {
    public CoteDto getByCoteId(long id);
    public Long saveCote(Cote cote, long userId);
    void updateCote(Cote cote, long postId, long userId);
    void deleteCote(long postId);


    default RowMapper<CoteDto> coteDtoRowMapper() {
        return ((rs, rowNum) -> {
            CoteDto coteDto=new CoteDto();
            coteDto.setId(rs.getLong("post_id"));
            coteDto.setUserId(rs.getString("user_id"));
            coteDto.setTitle(rs.getString("title"));
            coteDto.setContents(rs.getString("contents"));
            coteDto.setCreatedAt(rs.getTimestamp("created_at"));
            coteDto.setLike(rs.getInt("like"));
            coteDto.setAnswer(rs.getString("answer"));
            return coteDto;
        });
    };

    default RowMapper<TagDetailDto> TagRowMapper() {
        return (rs,rowNum)->{
            TagDetailDto tagDetailDto = new TagDetailDto();
            tagDetailDto.setId(rs.getLong("id"));
            tagDetailDto.setName(rs.getString("name"));
            return tagDetailDto;
        };
    }
}
