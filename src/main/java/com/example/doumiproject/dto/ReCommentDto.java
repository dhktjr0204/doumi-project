package com.example.doumiproject.dto;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReCommentDto {
    private long id;
    private long userId;
    private String author;
    private String contents;
    private java.sql.Timestamp createdAt;
    private long likeCount;
    private boolean isLiked;
    private int display;
}
