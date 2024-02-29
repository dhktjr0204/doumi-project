package com.example.doumiproject.service;

import com.example.doumiproject.dto.*;
import com.example.doumiproject.entity.Cote;
import com.example.doumiproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoteServiceImpl implements CoteService{

    private final PostRepository postRepository;
    private final CoteRepository quizRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

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
    public CoteDto getQuiz(long postId){

        return quizRepository.getByQuizId(postId);
    }

    @Override
    public List<CommentDto> getComments(long postId) {

        return commentRepository.getAllComment(postId);
    }

    @Override
    public List<TagDto> getAllTags() {

        return tagRepository.findAll();
    }

    //데이터 저장 도중 에러가 생길 경우 원 상태로 복귀
    @Transactional
    @Override
    public Long saveQuiz(Cote quiz, Long userId) {

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
    public void updateQuiz(Cote quiz, Long postId, Long userId) {

        quizRepository.updateQuiz(quiz, postId, userId);
    }

    @Override
    public void deleteQuiz(long postId) {
        
        quizRepository.deleteQuiz(postId);
    }

}
