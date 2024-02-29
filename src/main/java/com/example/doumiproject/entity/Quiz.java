package com.example.doumiproject.entity;

import com.example.doumiproject.dto.QuizDto;
import com.example.doumiproject.exception.quiz.QuizAnswerLengthException;
import com.example.doumiproject.exception.quiz.QuizContentsLengthException;
import com.example.doumiproject.exception.quiz.QuizTitleLengthException;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
public class Quiz {
    private String title; // 퀴즈 제목
    private String tags; // 선택된 태그 목록
    private String quizContent;
    private String answerContent;

    public Quiz(String title, String tags, String quizContent, String answerContent) {

        this.title = title;
        this.tags = tags;
        this.quizContent = quizContent;
        this.answerContent = answerContent;

        if (title.length() == 0 || title.length() > 250) {
            throw new QuizTitleLengthException();
        }
        if (quizContent.length() == 0 || quizContent.length() > 3000) {
            throw new QuizContentsLengthException();
        }
        if (answerContent.length() == 0 || answerContent.length() > 3000) {
            throw new QuizAnswerLengthException();
        }

    }
}
