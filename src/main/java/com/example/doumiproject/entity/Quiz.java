package com.example.doumiproject.entity;

import com.example.doumiproject.dto.QuizDto;
import com.example.doumiproject.exception.quiz.QuizAnswerLengthException;
import com.example.doumiproject.exception.quiz.QuizContentsLengthException;
import com.example.doumiproject.exception.quiz.QuizTitleLengthException;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    private long userId;//유저 고유 번호
    private String title; // 퀴즈 제목
    private String tags; // 선택된 태그 목록
    private String quizContent;
    private String answerContent;

    public Quiz(QuizDto quizDto) {
        String quizTitle = quizDto.getTitle();
        String quizContents = quizDto.getContents();
        String quizAnswer = quizDto.getAnswer();

        if (quizTitle.length() == 0 || quizTitle.length() > 251) {
            throw new QuizTitleLengthException();
        }
        if (quizContents.length() == 0 || quizContents.length() > 3000) {
            throw new QuizContentsLengthException();
        }
        if (quizAnswer.length() == 0 || quizAnswer.length() > 3000) {
            throw new QuizAnswerLengthException();
        }

    }
}
