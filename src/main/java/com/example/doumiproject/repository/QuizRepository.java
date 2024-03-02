package com.example.doumiproject.repository;

import com.example.doumiproject.dto.QuizDto;
import com.example.doumiproject.dto.TagDetailDto;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface QuizRepository {
    public QuizDto getQuizDetails(long post_id, long user_id);
    public QuizDto getByQuizId(long id);
    public Long saveQuiz(QuizRequestDto quiz, long userId);
    void updateQuiz(QuizRequestDto quiz, long postId);
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
            quizDto.setLikeCount(rs.getLong("like_count"));
            quizDto.setLiked(rs.getString("is_liked").equals("Y") ? true : false);
            quizDto.setAnswer(rs.getString("answer"));

            String[] tagNames = rs.getString("tag_names").split(",");
            String[] tagIds = rs.getString("tag_ids").split(",");

            List<Tag> tags = new ArrayList<>();
            for(int i = 0; i < tagNames.length; i++) {
                Tag tag = new Tag();
                tag.setId(Long.parseLong(tagIds[i]));
                tag.setName(tagNames[i]);
            }

            quizDto.setTags(tags);

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
