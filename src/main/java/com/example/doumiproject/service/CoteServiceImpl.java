package com.example.doumiproject.service;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.CoteDto;
import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.dto.TagDto;
import com.example.doumiproject.entity.Cote;
import com.example.doumiproject.repository.CommentRepository;
import com.example.doumiproject.repository.CoteRepository;
import com.example.doumiproject.repository.PostRepository;
import com.example.doumiproject.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoteServiceImpl implements CoteService{

    private final PostRepository postRepository;
    private final CoteRepository coteRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<PostDto> getAllCote(int page, int pageSize) {

        return postRepository.findAllPostWithType(page, pageSize, "COTE");
    }

    @Override
    public List<PostDto> getAllCote() {

        return postRepository.findAllPostWithType("COTE");
    }

    @Override
    public int getTotalPages(int pageSize) {

        return postRepository.getTotalPages(pageSize);
    }

    @Override
    public CoteDto getCote(long postId){

        return coteRepository.getByCoteId(postId);
    }

    @Override
    public List<CommentDto> getComments(long postId) {

        return commentRepository.getByQuizId(postId);
    }

    @Override
    public List<TagDto> getAllTags() {

        return tagRepository.findAll();
    }

    //데이터 저장 도중 에러가 생길 경우 원 상태로 복귀
    @Transactional
    @Override
    public Long saveCote(Cote cote, Long userId) {

        return coteRepository.saveCote(cote, userId);
    }

    @Override
    public int getTotalPages(int pageSize, String keyword) {

        return postRepository.getTotalPages(pageSize, keyword);
    }

    @Override
    public List<PostDto> getSearchCote(String keyword, int page, int pageSize) {

        return postRepository.findByTitleOrAuthor(keyword, page, pageSize);
    }

    @Transactional
    @Override
    public void updateCote(Cote quiz, Long postId, Long userId) {

        coteRepository.updateCote(quiz, postId, userId);
    }

    @Override
    public void deleteCote(long postId) {
        
        coteRepository.deleteCote(postId);
    }

}
