package com.example.doumiproject.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class QuizRequestDto {
    private long userId;//유저 고유 번호
    private String title; // 퀴즈 제목
    private String tags; // 선택된 태그 목록
    private String quizContent;
    private String answerContent;

    /*if (title.length() == 0 || title.length() > 250) {
            throw new QuizTitleLengthException();
        }
        if (quizContent.length() == 0 || quizContent.length() > 3000) {
            throw new QuizContentsLengthException();
        }
        if (answerContent.length() == 0 || answerContent.length() > 3000) {
            throw new QuizAnswerLengthException();
        }*/
}
