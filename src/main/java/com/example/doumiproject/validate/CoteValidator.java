package com.example.doumiproject.validate;

import com.example.doumiproject.dto.CoteRequestDto;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.exception.post.ContentsLengthException;
import com.example.doumiproject.exception.post.TitleLengthException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CoteValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {

        return CoteRequestDto.class.isAssignableFrom(clazz);

    }

    @Override
    public void validate(Object target, Errors errors) {
        CoteRequestDto coteRequestDto=(CoteRequestDto) target;
        String title=coteRequestDto.getTitle();
        String coteContent=coteRequestDto.getCoteContent();

        if (title.length() == 0 || title.length() > 250) {
            throw new TitleLengthException();
        }
        if (coteContent.length() == 0 || coteContent.length() > 3000) {
            throw new ContentsLengthException();
        }
    }
}
