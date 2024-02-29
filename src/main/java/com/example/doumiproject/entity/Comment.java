package com.example.doumiproject.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private long userId;
    private long postId;
    private String contents;
    private long parentCommentId;
    private boolean display;
}

