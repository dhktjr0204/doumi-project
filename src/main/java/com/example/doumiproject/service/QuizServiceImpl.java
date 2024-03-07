package com.example.doumiproject.service;

import com.example.doumiproject.dto.*;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.exception.post.NoContentException;
import com.example.doumiproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final PostRepository postRepository;
    private final QuizRepository quizRepository;
    private final TagRepository tagRepository;

    private String type = "QUIZ";

    @Override
    public List<PostDto> getAllQuiz(int page, int pageSize) {

        return postRepository.findAllPost(page, pageSize, type);
    }

    @Override
    public int getTotalPages(int pageSize) {

        return postRepository.getTotalPages(pageSize, type);
    }

    @Override
    public QuizDto getQuiz(long postId) {

        return quizRepository.getByQuizId(postId);
    }

    @Override
    public QuizDto getQuiz(long postId, long userId) {

        return quizRepository.getQuizDetails(postId, userId)
                .orElseThrow(NoContentException::new);
    }

    @Override
    public List<TagDto> getAllTags() {
        List<TagDto> tags = tagRepository.findAll();
        List<String> order = Arrays.asList("Java", "Spring", "DB", "AWS", "FrontEnd");
        //order로 정의한 순으로 정렬
        tags.sort(Comparator.comparingInt(tagDto -> order.indexOf(tagDto.getType())));

        return tags;
    }

    //데이터 저장 도중 에러가 생길 경우 원 상태로 복귀
    @Transactional
    @Override
    public Long saveQuiz(QuizRequestDto quiz, Long userId) {

        Long postId = quizRepository.saveQuiz(quiz, userId);

        quizRepository.saveAnswer(quiz, postId, userId);

        if (!quiz.getTags().isEmpty()) {
            String[] tags = quiz.getTags().split(",");
            tagRepository.saveTags(tags, postId);
        }

        return postId;
    }

    @Override
    public int getTotalPagesForSearch(int pageSize, String keyword) {

        return postRepository.getTotalPagesForSearch(pageSize, keyword, type);
    }

    @Override
    public List<PostDto> getSearchQuiz(String keyword, int page, int pageSize) {

        return postRepository.findByTitleOrAuthor(keyword, page, pageSize);
    }

    @Transactional
    @Override
    public void updateQuiz(QuizRequestDto quiz, Long postId) {

        quizRepository.updateQuiz(quiz, postId);
        quizRepository.updateAnswer(quiz, postId);

        //기존 태그 삭제 후 다시 저장
        tagRepository.deleteTags(postId);
        if (!quiz.getTags().isEmpty()) {
            String[] tags = quiz.getTags().split(",");
            tagRepository.saveTags(tags, postId);
        }
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
