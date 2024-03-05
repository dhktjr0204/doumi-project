package com.example.doumiproject.validate;

import com.example.doumiproject.dto.CoteRequestDto;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.exception.comment.CommentContentsLengthException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CommentValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {

        return Comment.class.isAssignableFrom(clazz);

    }

    @Override
    public void validate(Object target, Errors errors) {

        Comment comment=(Comment) target;
        String contents=comment.getContents();

        if (contents.length() == 0 || contents.length() > 1500) {
            throw new CommentContentsLengthException();
        }
    }
}
