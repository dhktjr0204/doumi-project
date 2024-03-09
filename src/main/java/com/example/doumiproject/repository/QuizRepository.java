package com.example.doumiproject.repository;

import com.example.doumiproject.dto.QuizDto;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.entity.Tag;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface QuizRepository {
    public Optional<QuizDto> getQuizDetails(long post_id, long user_id);

    public QuizDto findByQuizId(long id);

    public Long saveQuiz(QuizRequestDto quiz, long userId);

    public void saveAnswer(QuizRequestDto quiz, long postId, long userId);

    void updateQuiz(QuizRequestDto quiz, long postId);

    void updateAnswer(QuizRequestDto quiz, long postId);

    void deleteQuiz(long postId);

    List<Tag> getTags(long id);

    default RowMapper<QuizDto> quizDtoRowMapper() {

        return ((rs, rowNum) -> {

            String tagNames = rs.getString("tag_names");

            List<Tag> tags = new ArrayList<>();

            if (tagNames != null) {
                String[] tagNamesArray = tagNames.split(",");
                String[] tagIds = rs.getString("tag_ids").split(",");

                for (int i = 0; i < tagNamesArray.length; i++) {
                    Tag tag = new Tag();
                    tag.setId(Long.parseLong(tagIds[i]));
                    tag.setName(tagNamesArray[i]);
                    tags.add(tag);
                }
            }

            QuizDto quizDto = QuizDto.builder()
                    .id(rs.getLong("post_id"))
                    .userId(rs.getLong("user_id"))
                    .author(rs.getString("author"))
                    .title(rs.getString("title"))
                    .contents(rs.getString("contents"))
                    .postType(rs.getString("post_type"))
                    .createdAt(rs.getTimestamp("created_at"))
                    .updatedAt(rs.getTimestamp("updated_at"))
                    .likeCount(rs.getLong("like_count"))
                    .isLiked(rs.getString("is_liked").equals("Y") ? true : false)
                    .answer(rs.getString("answer"))
                    .tags(tags).build();

            return quizDto;
        });
    }

    default RowMapper<Tag> TagRowMapper() {
        return (rs, rowNum) -> {
            Tag tag = Tag.builder()
                    .id(rs.getLong("id"))
                    .name(rs.getString("name")).build();
            return tag;
        };
    }
}
