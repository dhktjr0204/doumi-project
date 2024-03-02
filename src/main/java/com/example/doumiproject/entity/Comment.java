package com.example.doumiproject.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    private long id;
    private long userId;
    private long postId;
    private String type;
    private String contents;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
    private boolean display;
    private long parentCommentId;

    public Comment(String type) {
        this.type = type;
    }
}

