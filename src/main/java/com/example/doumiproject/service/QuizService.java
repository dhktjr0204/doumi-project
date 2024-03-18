package com.example.doumiproject.service;

import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.dto.QuizDto;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.dto.TagDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface QuizService {
    List<PostDto> getAllQuiz(int page, int pageSize);

    int getTotalPages(int pageSize);

    QuizDto getQuiz(long postId);

    QuizDto getQuiz(long postId, long userId);

    List<TagDto> getAllTags();

    Long saveQuiz(QuizRequestDto quiz, Long userId);

    int getTotalPagesForSearch(int pageSize, String keyword);

    List<PostDto> getSearchQuiz(String keyword, int page, int pageSize);

    void updateQuiz(QuizRequestDto quiz, Long postId);

    void deleteQuiz(long postId);

    int getTotalPagesForSelectedTag(int pageSize, String tag);

    List<PostDto> getQuizForSelectedTag(String tag, int page, int pageSize);

    int getTotalPagesForMyPage(Long userId, String type, int pageSize);
    List<PostDto> findByUserId(Long userId, int page, int pageSize);
}

