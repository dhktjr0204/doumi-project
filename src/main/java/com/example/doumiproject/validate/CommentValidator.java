package com.example.doumiproject.validate;

import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.exception.comment.OverCommentLengthLimitException;
import com.example.doumiproject.exception.comment.EmptyCommentContentException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CommentValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {

        return Comment.class.isAssignableFrom(clazz);

    }

    @Override
    public void validate(Object target, Errors errors) {

        Comment comment = (Comment) target;
        String contents = comment.getContents();

        if (isEmptyComment(contents)) {
            throw new EmptyCommentContentException();
        }

        if (isOverCommentLengthLimit(contents)) {
            throw new OverCommentLengthLimitException();
        }
    }

    private boolean isEmptyComment(String contents) {
        return contents.isEmpty();
    }

    private boolean isOverCommentLengthLimit(String contents) {
        return contents.length() > 1500;
    }
}
