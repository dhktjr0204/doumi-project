package com.example.doumiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodingTestRequestDto {
    private long userId;
    private String title; // 질문 제목
    private String codingTestContent;
}
