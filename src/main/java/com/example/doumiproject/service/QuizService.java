package com.example.doumiproject.service;

import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.dto.QuizDto;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.dto.TagDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface QuizService {
    public List<PostDto> getAllQuiz(int page, int pageSize);
    public int getTotalPages(int pageSize);
    public QuizDto getQuiz(long postId);
    public QuizDto getQuiz(long postId, long userId);
    public List<TagDto> getAllTags();
    public Long saveQuiz(QuizRequestDto quiz, Long userId);
    public int getTotalPagesForSearch(int pageSize, String keyword);
    public List<PostDto> getSearchQuiz(String keyword, int page, int pageSize);
    public void updateQuiz(QuizRequestDto quiz, Long postId);
    public void deleteQuiz(long postId);
    public int getTotalPagesForSelectedTag(int pageSize, String tag);
    public List<PostDto> getQuizForSelectedTag(String tag, int page, int pageSize);
}

