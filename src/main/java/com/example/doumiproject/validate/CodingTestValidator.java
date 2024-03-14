package com.example.doumiproject.validate;

import com.example.doumiproject.dto.CodingTestRequestDto;
import com.example.doumiproject.exception.post.EmptyContentException;
import com.example.doumiproject.exception.post.EmptyTitleException;
import com.example.doumiproject.exception.post.OverContentLengthLimitException;
import com.example.doumiproject.exception.post.OverTitleLengthLimitException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CodingTestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {

        return CodingTestRequestDto.class.isAssignableFrom(clazz);

    }

    @Override
    public void validate(Object target, Errors errors) {
        CodingTestRequestDto codingTestRequestDto =(CodingTestRequestDto) target;
        String title= codingTestRequestDto.getTitle();
        String coteContent= codingTestRequestDto.getCodingTestContent();

        if(isEmptyTitle(title)){
            throw new EmptyTitleException();
        }
        if(isOverTitleLengthLimit(title)) {
            throw new OverTitleLengthLimitException();
        }

        if(isEmptyCodingTestContent(coteContent)){
            throw new EmptyContentException();
        }
        if (isOverCodingTestContentLengthLimit(coteContent)) {
            throw new OverContentLengthLimitException();
        }
    }

    private boolean isEmptyTitle(String title) {
        return title.isEmpty();
    }

    private boolean isOverTitleLengthLimit(String title) {
        return title.length() > 250;
    }

    private boolean isEmptyCodingTestContent(String content) {
        return content.isEmpty();
    }

    private boolean isOverCodingTestContentLengthLimit(String content) {
        return content.length() > 3000;
    }
}
