package com.example.doumiproject.entity;

import lombok.*;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Post {

    private long id;
    private long userId;
    private String type;
    private String title;
    private String contents;
    private java.sql.Timestamp createdAt;
    private java.sql.Timestamp updatedAt;
}
