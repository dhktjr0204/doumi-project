package com.example.doumiproject.service;

import com.example.doumiproject.dto.CodingTestRequestDto;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.exception.post.NoContentException;
import com.example.doumiproject.repository.CodingTestRepository;
import com.example.doumiproject.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class CodingTestServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private CodingTestRepository codingTestRepository;
    @InjectMocks
    private CodingTestServiceImpl codingTestService;

    @Test
    @DisplayName("존재 하지 않는 게시물 가져오기")
    void getQuiz_withInValidData() {
        //given
        long postId = 1;
        long userId = 1;

        when(codingTestRepository.findByCodingTestId(postId, userId))
                .thenReturn(Optional.empty());

        //then
        //예외 발생 됐는지 확인
        assertThatThrownBy(() -> codingTestService.getCodingTest(postId, userId))
                .isInstanceOf(NoContentException.class).hasMessage(null);
    }

    @Test
    @DisplayName("저장 테스트")
    void saveCodingTest(){
        //given
        Long postId=1L;
        long userId=1;

        CodingTestRequestDto cote = CodingTestRequestDto.builder()
                .userId(userId)
                .title("Sample Title")
                .codingTestContent("Sample Quiz Content")
                .build();

        when(codingTestRepository.saveCodingTest(cote, userId)).thenReturn(postId);

        //when
        Long result = codingTestService.saveCodingTest(cote, userId);

        //then
        assertThat(result).isEqualTo(postId);
    }


}