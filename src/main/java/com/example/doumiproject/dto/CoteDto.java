package com.example.doumiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoteDto {
    private long id;
    private long userId;
    private String author;
    private String title;
    private String contents;
    private String postType;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
    private long likeCount;
    private boolean isLiked;
}
