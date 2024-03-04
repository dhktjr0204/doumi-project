package com.example.doumiproject.repository;

import com.example.doumiproject.dto.TagDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JdbcTemplateTagRepository implements TagRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateTagRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<TagDto> findAll() {
        String sql = "select type, GROUP_CONCAT(name ORDER BY id) as names, GROUP_CONCAT(id ORDER BY id) as ids " +
                "from tag " +
                "group by type";
        return jdbcTemplate.query(sql, TagRowMapper());
    }

    @Override
    public void saveTags(String[] tags, long postId) {
        //태그 저장
        String tagSql = "insert into quiztag (post_id, tag_id) " +
                "values (?,?)";

        for (String tag : tags) {
            jdbcTemplate.update(tagSql, postId, Integer.parseInt(tag));
        }
    }

    @Override
    public void deleteTags(long postId) {
        // 기존 태그 삭제 후 새로운 태그 추가
        String deleteTagsSql = "delete from quiztag where post_id = ?";
        jdbcTemplate.update(deleteTagsSql, postId);
    }


}
