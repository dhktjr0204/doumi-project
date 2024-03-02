package com.example.doumiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoteRequestDto {
    private String title; // 질문 제목
    private String coteContent;
}
