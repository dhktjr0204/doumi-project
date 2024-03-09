package com.example.doumiproject.service;

import com.example.doumiproject.dto.CoteDto;
import com.example.doumiproject.dto.CoteRequestDto;
import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.dto.TagDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface CoteService {
    public List<PostDto> getAllCote(int page, int pageSize);
    public int getTotalPages(int pageSize);
    public CoteDto getCote(long postId,long userId);
    public List<TagDto> getAllTags();
    public Long saveCote(CoteRequestDto cote, Long userId);
    public int getTotalPagesForSearch(int pageSize, String keyword);
    public List<PostDto> getSearchCote(String keyword, int page, int pageSize);
    public void updateCote(CoteRequestDto cote, Long postId);
    public void deleteCote(long postId);
}

