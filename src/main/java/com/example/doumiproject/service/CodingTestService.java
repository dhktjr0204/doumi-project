package com.example.doumiproject.service;

import com.example.doumiproject.dto.CodingTestDto;
import com.example.doumiproject.dto.CodingTestRequestDto;
import com.example.doumiproject.dto.PostDto;

import java.util.List;

public interface CodingTestService {
    public List<PostDto> getAllCodingTest(int page, int pageSize);
    public int getTotalPages(int pageSize);
    public CodingTestDto getCodingTest(long postId, long userId);
    public Long saveCodingTest(CodingTestRequestDto cote, Long userId);
    public int getTotalPagesForSearch(int pageSize, String keyword);
    public List<PostDto> getSearchCodingTest(String keyword, int page, int pageSize);
    public void updateCodingTest(CodingTestRequestDto cote, Long postId);
    public void deleteCodingTest(long postId);
}

