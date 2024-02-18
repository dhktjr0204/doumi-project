package com.example.doumiproject.repository;

import com.example.doumiproject.dto.QuizDto;
import org.springframework.jdbc.core.RowMapper;

public interface QuizRepository {
    public QuizDto getByQuizId(long id);

    default RowMapper<QuizDto> quizDtoRowMapper() {
        return ((rs, rowNum) -> {
            QuizDto quizDto=new QuizDto();
            quizDto.setId(rs.getLong("post_id"));
            quizDto.setUserId(rs.getString("nickname"));
            quizDto.setTitle(rs.getString("title"));
            quizDto.setContents(rs.getString("contents"));
            quizDto.setCreatedAt(rs.getTimestamp("created_at"));
            quizDto.setLike(rs.getInt("like"));
            quizDto.setAnswer(rs.getString("answer"));
            return quizDto;
        });
    };
}
