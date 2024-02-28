package com.example.doumiproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cote {
    private String title; // 질문 제목
    private String tags; // 선택된 태그 목록
    private String coteContent;
    private String answerContent;
}
