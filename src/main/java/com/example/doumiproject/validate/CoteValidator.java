package com.example.doumiproject.validate;

import com.example.doumiproject.dto.CoteRequestDto;
import com.example.doumiproject.exception.post.EmptyContentException;
import com.example.doumiproject.exception.post.OverContentLengthLimitException;
import com.example.doumiproject.exception.post.OverTitleLengthLimitException;
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

        if(isEmptyTitle(title)){
            throw new RuntimeException();
        }
        if(isOverTitleLengthLimit(title)) {
            throw new OverTitleLengthLimitException();
        }

        if(isEmptyCoteContent(coteContent)){
            throw new EmptyContentException();
        }
        if (isOverCoteContentLengthLimit(coteContent)) {
            throw new OverContentLengthLimitException();
        }
    }

    private boolean isEmptyTitle(String title) {
        return title.isEmpty();
    }

    private boolean isOverTitleLengthLimit(String title) {
        return title.length() > 250;
    }

    private boolean isEmptyCoteContent(String content) {
        return content.isEmpty();
    }

    private boolean isOverCoteContentLengthLimit(String content) {
        return content.length() > 3000;
    }
}
