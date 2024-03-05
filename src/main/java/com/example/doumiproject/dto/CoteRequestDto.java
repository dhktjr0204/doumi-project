package com.example.doumiproject.dto;

import com.example.doumiproject.exception.post.ContentsLengthException;
import com.example.doumiproject.exception.post.TitleLengthException;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CoteRequestDto {
    private String title; // 질문 제목
    private String coteContent;

    public CoteRequestDto(String title, String coteContent) {
        if (title.length() == 0 || title.length() > 250) {
            throw new TitleLengthException();
        }
        if (coteContent.length() == 0 || coteContent.length() > 3000) {
            throw new ContentsLengthException();
        }

        this.title = title;
        this.coteContent = coteContent;
    }
}
