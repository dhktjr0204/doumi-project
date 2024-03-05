package com.example.doumiproject.dto;

import com.example.doumiproject.exception.quiz.QuizAnswerLengthException;
import com.example.doumiproject.exception.post.ContentsLengthException;
import com.example.doumiproject.exception.post.TitleLengthException;
import lombok.*;

@Data
@NoArgsConstructor
public class QuizRequestDto {
    private long userId;//유저 고유 번호
    private String title; // 퀴즈 제목
    private String tags; // 선택된 태그 목록
    private String quizContent;
    private String answerContent;

    public QuizRequestDto(long userId, String title, String tags, String quizContent, String answerContent) {
        if (title.length() == 0 || title.length() > 250) {
            throw new TitleLengthException();
        }
        if (quizContent.length() == 0 || quizContent.length() > 3000) {
            throw new ContentsLengthException();
        }
        if (answerContent.length() == 0 || answerContent.length() > 3000) {
            throw new QuizAnswerLengthException();
        }

        this.userId = userId;
        this.title = title;
        this.tags = tags;
        this.quizContent = quizContent;
        this.answerContent = answerContent;
    }
}
