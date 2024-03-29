package com.example.doumiproject.dto;

import com.example.doumiproject.entity.Tag;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class QuizDto {
    private long id;
    private long userId;
    private String author;
    private String title;
    private String contents;
    private String answer;
    private String postType;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
    private List<Tag> tags;
    private long likeCount;
    private boolean isLiked;
}
