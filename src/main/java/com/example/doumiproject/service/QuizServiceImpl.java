package com.example.doumiproject.service;

import com.example.doumiproject.dto.*;
import com.example.doumiproject.entity.Quiz;
import com.example.doumiproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService{

    private final PostRepository postRepository;
    private final QuizRepository quizRepository;
    private final TagRepository tagRepository;

    @Override
    public List<PostDto> getAllQuiz(int page, int pageSize) {

        return postRepository.findAllQuiz(page, pageSize);
    }

    @Override
    public List<PostDto> getAllQuiz() {

        return postRepository.findAllQuiz();
    }

    @Override
    public int getTotalPages(int pageSize) {

        return postRepository.getTotalPages(pageSize);
    }

    @Override
    public QuizDto getQuiz(long postId) {

        return quizRepository.getByQuizId(postId);
    }

    @Override
    public QuizDto getQuiz(long postId, long userId){

        return quizRepository.getQuizDetails(postId, userId);
    }

    @Override
    public List<TagDto> getAllTags() {

        return tagRepository.findAll();
    }

    //데이터 저장 도중 에러가 생길 경우 원 상태로 복귀
    @Transactional
    @Override
    public Long saveQuiz(Quiz quiz, Long userId) {

        return quizRepository.saveQuiz(quiz, userId);
    }

    @Override
    public int getTotalPages(int pageSize, String keyword) {

        return postRepository.getTotalPages(pageSize, keyword);
    }

    @Override
    public List<PostDto> getSearchQuiz(String keyword, int page, int pageSize) {

        return postRepository.findByTitleOrAuthor(keyword, page, pageSize);
    }

    @Transactional
    @Override
    public void updateQuiz(Quiz quiz, Long postId) {

        quizRepository.updateQuiz(quiz, postId);
    }

    @Override
    public void deleteQuiz(long postId) {
        
        quizRepository.deleteQuiz(postId);
    }

    @Override
    public int getTotalPagesForSelectedTag(int pageSize, String tag) {

        return postRepository.getTotalPagesForTag(pageSize, tag);
    }

    @Override
    public List<PostDto> getQuizForSelectedTag(String tag, int page, int pageSize) {

        return postRepository.findByTag(tag, page, pageSize);
    }

}
