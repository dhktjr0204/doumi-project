package com.example.doumiproject.repository;

import com.example.doumiproject.dto.QuizDto;
import com.example.doumiproject.dto.TagDetailDto;
import com.example.doumiproject.entity.Quiz;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.List;

public interface QuizRepository {
    public QuizDto getQuizDetails(long post_id, long user_id);
    public QuizDto getByQuizId(long id);
    public Long saveQuiz(Quiz quiz, long userId);
    void updateQuiz(Quiz quiz, long postId);
    void deleteQuiz(long postId);

    List<TagDetailDto> getTags(long id);

    default RowMapper<QuizDto> quizDtoRowMapper() {
        return ((rs, rowNum) -> {
            QuizDto quizDto=new QuizDto();
            quizDto.setId(rs.getLong("post_id"));
            quizDto.setUserId(rs.getLong("user_id"));
            quizDto.setAuthor(rs.getString("author"));
            quizDto.setTitle(rs.getString("title"));
            quizDto.setContents(rs.getString("contents"));
            quizDto.setPostType(rs.getString("post_type"));
            quizDto.setCreatedAt(rs.getTimestamp("created_at"));
            quizDto.setUpdatedAt(rs.getTimestamp("updated_at"));
            quizDto.setTags(Arrays.stream(rs.getString("tag_names").split(",")).toList());
            quizDto.setLikeCount(rs.getLong("like_count"));
            quizDto.setLiked(rs.getString("is_liked").equals("Y") ? true : false);
            quizDto.setAnswer(rs.getString("answer"));
            return quizDto;
        });
    }

    default RowMapper<TagDetailDto> TagRowMapper() {
        return (rs,rowNum)->{
            TagDetailDto tagDetailDto = new TagDetailDto();
            tagDetailDto.setId(rs.getLong("id"));
            tagDetailDto.setName(rs.getString("name"));
            return tagDetailDto;
        };
    }
}
