package com.example.doumiproject.controller;

import com.example.doumiproject.dto.CodingTestRequestDto;
import com.example.doumiproject.service.CommentService;
import com.example.doumiproject.service.CodingTestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(CodingTestController.class)
class CodingTestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CodingTestService codingTestService;
    @MockBean
    private CommentService commentService;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USER_NAME = "testuser";

    private MockHttpSession session;

    @BeforeEach
    public void setup() {
        session = new MockHttpSession();
        // 테스트 전에 세션에 userId와 userName 저장
        session.setAttribute("userId", TEST_USER_ID);
        session.setAttribute("userName", TEST_USER_NAME);
    }

    @Test
    @DisplayName("저장 테스트")
    void postCote() throws Exception {
        CodingTestRequestDto codingTestRequestDto = CodingTestRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("Sample Title")
                .codingTestContent("Sample coteContent")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/codingtest/post")
                        .session(session)
                        .param("userId", Long.toString(codingTestRequestDto.getUserId()))
                        .param("title", codingTestRequestDto.getTitle())
                        .param("codingTestContent", codingTestRequestDto.getCodingTestContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("수정 테스트")
    void editCote() throws Exception {
        long postId=1;

        CodingTestRequestDto codingTestRequestDto = CodingTestRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("Sample Title")
                .codingTestContent("Sample coteContent")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/codingtest/edit")
                        .session(session)
                        .param("id", String.valueOf(postId))
                        .param("userId", Long.toString(codingTestRequestDto.getUserId()))
                        .param("title", codingTestRequestDto.getTitle())
                        .param("codingTestContent", codingTestRequestDto.getCodingTestContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("권한 없는 유저가 수정 요청 했을 때 테스트")
    void editCote_withValidUser() throws Exception {
        long postId=1;
        long userId=2;

        CodingTestRequestDto codingTestRequestDto = CodingTestRequestDto.builder()
                .userId(userId)
                .title("Sample Title")
                .codingTestContent("Sample coteContent")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/codingtest/edit")
                        .session(session)
                        .param("id", String.valueOf(postId))
                        .param("userId", Long.toString(codingTestRequestDto.getUserId()))
                        .param("title", codingTestRequestDto.getTitle())
                        .param("codingTestContent", codingTestRequestDto.getCodingTestContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteCote() throws Exception {
        long postId=1;
        long userId=TEST_USER_ID;

        mockMvc.perform(MockMvcRequestBuilders.delete("/codingtest/delete")
                        .session(session)
                        .param("postId", String.valueOf(postId))
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("권한이 없는 유저가 삭제 요청 했을 때 테스트")
    void deleteCote_withValidUser() throws Exception {
        long postId=1;
        long userId=2;

        mockMvc.perform(MockMvcRequestBuilders.delete("/codingtest/delete")
                        .session(session)
                        .param("postId", String.valueOf(postId))
                        .param("userId", String.valueOf(userId))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("title이 250자 초과하였을 때 테스트")
    void postCote_withValidTitleLength() throws Exception {
        CodingTestRequestDto codingTestRequestDto = CodingTestRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("Sample TitleSample TitleSample".repeat(10))
                .codingTestContent("Sample coteContent")
                .build();

        String expectedMessage="제목 길이가 최대 길이를 초과하였습니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/codingtest/post")
                        .session(session)
                        .param("userId", Long.toString(codingTestRequestDto.getUserId()))
                        .param("title", codingTestRequestDto.getTitle())
                        .param("codingTestContent", codingTestRequestDto.getCodingTestContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("title이 빈칸일 때 테스트")
    void postCote_withValidEmptyTitle() throws Exception {
        CodingTestRequestDto codingTestRequestDto = CodingTestRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("")
                .codingTestContent("Sample coteContent")
                .build();

        String expectedMessage="제목이 비어있습니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/codingtest/post")
                        .session(session)
                        .param("userId", Long.toString(codingTestRequestDto.getUserId()))
                        .param("title", codingTestRequestDto.getTitle())
                        .param("codingTestContent", codingTestRequestDto.getCodingTestContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("contents가 3000자 초과하였을 때 테스트")
    void postCote_withValidContentLength() throws Exception {
        CodingTestRequestDto codingTestRequestDto = CodingTestRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("Sample Title")
                .codingTestContent("Sample coteContentSample coteContentSample coteContentSample coteContentSample coteContentSample coteContent".repeat(40))
                .build();

        String expectedMessage="본문 길이가 최대 길이를 초과하였습니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/codingtest/post")
                        .session(session)
                        .param("userId", Long.toString(codingTestRequestDto.getUserId()))
                        .param("title", codingTestRequestDto.getTitle())
                        .param("codingTestContent", codingTestRequestDto.getCodingTestContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("contents가 빈칸일 때 테스트")
    void postCote_withValidEmptyContents() throws Exception {
        CodingTestRequestDto codingTestRequestDto = CodingTestRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("Sample Title")
                .codingTestContent("")
                .build();

        String expectedMessage="본문이 비어있습니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/codingtest/post")
                        .session(session)
                        .param("userId", Long.toString(codingTestRequestDto.getUserId()))
                        .param("title", codingTestRequestDto.getTitle())
                        .param("codingTestContent", codingTestRequestDto.getCodingTestContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }
}