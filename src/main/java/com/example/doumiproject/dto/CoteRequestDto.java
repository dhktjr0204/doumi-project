package com.example.doumiproject.dto;

import com.example.doumiproject.exception.quiz.QuizContentsLengthException;
import com.example.doumiproject.exception.quiz.QuizTitleLengthException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoteRequestDto {
    private String title; // 질문 제목
    private String coteContent;

    public CoteRequestDto(String title, String coteContent) {
        if (title.length() == 0 || title.length() > 250) {
            throw new QuizTitleLengthException();
        }
        if (coteContent.length() == 0 || coteContent.length() > 3000) {
            throw new QuizContentsLengthException();
        }

        this.title = title;
        this.coteContent = coteContent;
    }
}
