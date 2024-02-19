package com.example.doumiproject.dto;

import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDto {
    private long id;
    private String userId;
    private String contents;
    private java.sql.Timestamp createdAt;
    private long like;
    private int display;
    private List<ReCommentDto> reComments;
}
