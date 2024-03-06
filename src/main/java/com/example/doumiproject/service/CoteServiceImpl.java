package com.example.doumiproject.service;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.CoteDto;
import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.dto.TagDto;
import com.example.doumiproject.dto.CoteRequestDto;
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
public class CoteServiceImpl implements CoteService {

    private final PostRepository postRepository;
    private final CoteRepository coteRepository;
    private final TagRepository tagRepository;
    private final CommentRepository commentRepository;

    private final String type = "COTE";

    @Override
    public List<PostDto> getAllCote(int page, int pageSize) {

        return postRepository.findAllPost(page, pageSize, type);
    }

    @Override
    public int getTotalPages(int pageSize) {

        return postRepository.getTotalPages(pageSize, type);
    }

    @Override
    public CoteDto getCote(long postId, long userId) {

        return coteRepository.getByCoteId(postId, userId);
    }

    @Override
    public List<CommentDto> getComments(long postId, long userId) {

        return commentRepository.getAllComment(postId, userId);
    }

    @Override
    public List<TagDto> getAllTags() {

        return tagRepository.findAll();
    }

    //데이터 저장 도중 에러가 생길 경우 원 상태로 복귀
    @Transactional
    @Override
    public Long saveCote(CoteRequestDto cote, Long userId) {
        CoteRequestDto coteRequestDto = new CoteRequestDto(cote.getTitle(), cote.getCoteContent());

        return coteRepository.saveCote(coteRequestDto, userId);
    }

    @Override
    public int getTotalPagesForSearch(int pageSize, String keyword) {

        return postRepository.getTotalPagesForSearch(pageSize, keyword, type);
    }

    @Override
    public List<PostDto> getSearchCote(String keyword, int page, int pageSize) {

        return postRepository.findByTitleOrAuthor(keyword, type, page, pageSize);
    }

    @Transactional
    @Override
    public void updateCote(CoteRequestDto cote, Long postId) {
        CoteRequestDto coteRequestDto = new CoteRequestDto(cote.getTitle(), cote.getCoteContent());

        coteRepository.updateCote(coteRequestDto, postId);
    }

    @Override
    public void deleteCote(long postId) {

        coteRepository.deleteCote(postId);
    }

}
