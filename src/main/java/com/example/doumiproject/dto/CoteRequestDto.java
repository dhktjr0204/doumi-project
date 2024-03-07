package com.example.doumiproject.dto;

import com.example.doumiproject.exception.post.ContentsLengthException;
import com.example.doumiproject.exception.post.TitleLengthException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoteRequestDto {
    private long userId;
    private String title; // 질문 제목
    private String coteContent;
}
