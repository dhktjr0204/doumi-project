package com.example.doumiproject.service;

import com.example.doumiproject.dto.*;
import com.example.doumiproject.entity.Quiz;

import java.util.List;

public interface QuizService {
    public List<PostDto> getAllQuiz(int page, int pageSize);
    public List<PostDto> getAllQuiz();
    public int getTotalPages(int pageSize);
    public QuizDto getQuiz(long postId);
    public List<TagDto> getAllTags();
    public Long saveQuiz(QuizDto quiz, Long userId);
    public int getTotalPages(int pageSize, String keyword);
    public List<PostDto> getSearchQuiz(String keyword, int page, int pageSize);
    public void updateQuiz(QuizDto quiz, Long postId, Long userId);
    public void deleteQuiz(long postId);
    public int getTotalPagesForSelectedTag(int pageSize, String tag);
    public List<PostDto> getQuizForSelectedTag(String tag, int page, int pageSize);
}

