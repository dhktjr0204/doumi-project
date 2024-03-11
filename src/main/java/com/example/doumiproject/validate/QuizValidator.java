package com.example.doumiproject.validate;


import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.exception.post.EmptyContentException;
import com.example.doumiproject.exception.post.EmptyTitleException;
import com.example.doumiproject.exception.post.OverContentLengthLimitException;
import com.example.doumiproject.exception.post.OverTitleLengthLimitException;
import com.example.doumiproject.exception.quiz.OverAnswerLengthLimitException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


public class QuizValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {

        return QuizRequestDto.class.isAssignableFrom(clazz);

    }

    @Override
    public void validate(Object target, Errors errors) {
        QuizRequestDto quiz = (QuizRequestDto) target;

        String title=quiz.getTitle();
        String quizContent= quiz.getQuizContent();
        String answerContent=quiz.getAnswerContent();

        if(isEmptyTitle(title)){
            throw new EmptyTitleException();
        }
        if(isOverTitleLengthLimit(title)){
            throw new OverTitleLengthLimitException();
        }
        if(isEmptyQuizContent(quizContent)){
            throw new EmptyContentException();
        }
        if(isOverQuizContentLengthLimit(quizContent)){
            throw new OverContentLengthLimitException();
        }
        if(isOverAnswerContentLengthLimit(answerContent)){
            throw new OverAnswerLengthLimitException();
        }
    }

    private boolean isEmptyTitle(String title) {
        return title.isEmpty();
    }

    private boolean isOverTitleLengthLimit(String title) {
        return title.length() > 250;
    }

    private boolean isEmptyQuizContent(String content) {
        return content.isEmpty();
    }

    private boolean isOverQuizContentLengthLimit(String content) {
        return content.length() > 3000;
    }

    private boolean isOverAnswerContentLengthLimit(String content) {
        return content.length() > 3000;
    }
}
