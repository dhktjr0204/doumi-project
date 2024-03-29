package com.example.doumiproject.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDto {
    private long id;
    private long userId;
    private long postId;
    private String author;
    private String type;
    private String contents;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
    private long likeCount;
    private boolean isLiked;
    private int display;
    private List<ReCommentDto> reComments;
}
