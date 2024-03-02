package com.example.doumiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReCommentDto {
    private long id;
    private long userId;
    private String author;
    private String type;
    private String contents;
    private java.sql.Timestamp createdAt;
    private long likeCount;
    private boolean isLiked;
    private int display;
}
