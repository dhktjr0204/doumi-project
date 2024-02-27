package com.example.doumiproject.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    private long userId;//유저 고유 번호
    private String title; // 퀴즈 제목
    private String tags; // 선택된 태그 목록
    private String quizContent;
    private String answerContent;
}
