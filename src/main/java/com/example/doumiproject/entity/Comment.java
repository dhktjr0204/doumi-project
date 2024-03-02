package com.example.doumiproject.entity;

import com.example.doumiproject.exception.comment.CommentContentsLengthException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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

    public Comment(long userId, long postId, String type, String contents, boolean display, long parentCommentId) {
        if (contents.length() == 0 || contents.length() > 250) {
            throw new CommentContentsLengthException();
        }

        this.userId = userId;
        this.postId = postId;
        this.type = type;
        this.contents = contents;
        this.display = display;
        this.parentCommentId = parentCommentId;
    }
}

