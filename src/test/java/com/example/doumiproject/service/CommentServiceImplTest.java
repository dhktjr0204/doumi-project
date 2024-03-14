package com.example.doumiproject.service;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.ReCommentDto;
import com.example.doumiproject.repository.CommentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentServiceImpl commentService;

    private CommentDto createDummyCommentDto(long id){
        return CommentDto.builder()
                .id(id)
                .userId(1L)
                .author("Dummy Author")
                .type("Dummy Type")
                .contents("Dummy Contents")
                .createdAt(Timestamp.valueOf("2022-01-01 00:00:00"))
                .likeCount(0)
                .isLiked(false)
                .display(1)
                .build();
    }

    private ReCommentDto createDummyRecommentDto(long id){
        return ReCommentDto.builder()
                .id(id)
                .userId(1L)
                .author("Dummy Author")
                .type("Dummy Type")
                .contents("Dummy Contents")
                .createdAt(Timestamp.valueOf("2022-01-01 00:00:00"))
                .likeCount(0)
                .isLiked(false)
                .display(1)
                .build();
    }

    private CommentDto createDummyCommentDtoWithRecommentDto(long id, List<Integer> reCommentIds){
        List<ReCommentDto> reCommentDto=new ArrayList<>();

        for(int reCommentId : reCommentIds){
            reCommentDto.add(createDummyRecommentDto(reCommentId));
        }

        return CommentDto.builder()
                .id(id)
                .userId(1L)
                .author("Dummy Author")
                .type("Dummy Type")
                .contents("Dummy Contents")
                .createdAt(Timestamp.valueOf("2022-01-01 00:00:00"))
                .likeCount(0)
                .isLiked(false)
                .display(1)
                .reComments(reCommentDto)
                .build();
    }

    @Test
    @DisplayName("댓글 가져오기")
    void getAllComments(){
        //given
        long postId=1;
        long userId=1;

        List<CommentDto> dummyCommentDto = Collections.singletonList(createDummyCommentDto(1));
        List<ReCommentDto> dummyRecommentDto= Arrays.asList(
                createDummyRecommentDto(2),
                createDummyRecommentDto(3)
        );

        //댓글에 대댓글 담긴 형태 생성
        List<CommentDto> expectedResult= Collections.singletonList(
                createDummyCommentDtoWithRecommentDto(1, Arrays.asList(2, 3)));

        when(commentRepository.getAllComment(postId,userId)).thenReturn(dummyCommentDto);
        when(commentRepository.getAllReComment(dummyCommentDto.get(0).getId(),userId)).thenReturn(dummyRecommentDto);

        //when
        List<CommentDto> result = commentService.getAllComments(postId, userId);

        //then
        assertThat(result).isEqualTo(expectedResult);
    }
}