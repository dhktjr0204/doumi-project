package com.example.doumiproject.validate;


import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.exception.post.ContentsLengthException;
import com.example.doumiproject.exception.post.TitleLengthException;
import com.example.doumiproject.exception.quiz.QuizAnswerLengthException;
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

        if (title.length() == 0 || title.length() > 250) {
            throw new TitleLengthException();
        }
        if (quizContent.length() == 0 || quizContent.length() > 3000) {
            throw new ContentsLengthException();
        }
        if (answerContent.length() > 3000) {
            throw new QuizAnswerLengthException();
        }
    }
}
