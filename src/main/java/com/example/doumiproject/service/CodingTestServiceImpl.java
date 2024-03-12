package com.example.doumiproject.service;

import com.example.doumiproject.dto.CodingTestDto;
import com.example.doumiproject.dto.CodingTestRequestDto;
import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.exception.post.NoContentException;
import com.example.doumiproject.repository.CodingTestRepository;
import com.example.doumiproject.repository.PostRepository;
import com.example.doumiproject.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodingTestServiceImpl implements CodingTestService {

    private final PostRepository postRepository;
    private final CodingTestRepository codingTestRepository;

    private final String type = "COTE";

    @Override
    public List<PostDto> getAllCodingTest(int page, int pageSize) {

        return postRepository.findAllPost(page, pageSize, type);
    }

    @Override
    public int getTotalPages(int pageSize) {

        return postRepository.getTotalPages(pageSize, type);
    }

    @Override
    public CodingTestDto getCodingTest(long postId, long userId) {

        return codingTestRepository.findByCodingTestId(postId, userId)
                .orElseThrow(NoContentException::new);
    }

    //데이터 저장 도중 에러가 생길 경우 원 상태로 복귀
    @Transactional
    @Override
    public Long saveCodingTest(CodingTestRequestDto cote, Long userId) {

        return codingTestRepository.saveCodingTest(cote, userId);
    }

    @Override
    public int getTotalPagesForSearch(int pageSize, String keyword) {

        return postRepository.getTotalPagesForSearch(pageSize, keyword, type);
    }

    @Override
    public List<PostDto> getSearchCodingTest(String keyword, int page, int pageSize) {

        return postRepository.findByTitleOrAuthor(keyword, type, page, pageSize);
    }

    @Transactional
    @Override
    public void updateCodingTest(CodingTestRequestDto cote, Long postId) {

        codingTestRepository.updateCodingTest(cote, postId);
    }

    @Override
    public void deleteCodingTest(long postId) {

        codingTestRepository.deleteCodingTest(postId);
    }
}
