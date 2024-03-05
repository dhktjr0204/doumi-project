package com.example.doumiproject.controller;

import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.service.CommentService;
import com.example.doumiproject.service.QuizService;
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

@WebMvcTest(QuizController.class)
class QuizControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuizService quizService;
    @MockBean
    private CommentService commentService;

    private static final Long TEST_USER_ID = 1L;
    private static final String TEST_USER_NAME = "testuser";

    private MockHttpSession session;
    @BeforeEach
    public void setup() throws Exception{
        session = new MockHttpSession();
        // 테스트 전에 세션에 userId와 userName 저장
        session.setAttribute("userId", TEST_USER_ID);
        session.setAttribute("userName", TEST_USER_NAME);
    }

    @Test
    @DisplayName("contents가 3000자 이상일 때 테스트")
    void postQuiz_withInvalidQuizCountentLength() throws Exception{

        QuizRequestDto quizRequestDto=QuizRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("Sample Title")
                .quizContent("Sample quiz content with more than 3000 characters. Sample quiz content with more than 3000 characters.\n".repeat(40))
                .answerContent("Sample Anwser Content")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/quiz/post")
                        .session(session)
                        .param("userId", Integer.toString((int) quizRequestDto.getUserId()))
                        .param("title", quizRequestDto.getTitle())
                        .param("quizContent", quizRequestDto.getQuizContent())
                        .param("answerContent", quizRequestDto.getAnswerContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("title이 아무것도 없을 때")
    void postQuiz_withValidTitleNull() throws Exception {
        QuizRequestDto quizRequestDto=QuizRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("")
                .quizContent("Sample quiz content with more than 3000 characters. Sample quiz content with more than 3000 characters.\n".repeat(40))
                .answerContent("Sample Anwser Content")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/quiz/post")
                        .session(session)
                        .param("userId", Integer.toString((int) quizRequestDto.getUserId()))
                        .param("title", quizRequestDto.getTitle())
                        .param("quizContent", quizRequestDto.getQuizContent())
                        .param("answerContent", quizRequestDto.getAnswerContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

    @Test
    @DisplayName("title이 250글자 넘어갈때")
    void postQuiz_withValidTitleLength() throws Exception {
        QuizRequestDto quizRequestDto=QuizRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요")
                .quizContent("Sample quiz content with more than 3000 characters. Sample quiz content with more than 3000 characters.\n".repeat(40))
                .answerContent("Sample Anwser Content")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/quiz/post")
                        .session(session)
                        .param("userId", Integer.toString((int) quizRequestDto.getUserId()))
                        .param("title", quizRequestDto.getTitle())
                        .param("quizContent", quizRequestDto.getQuizContent())
                        .param("answerContent", quizRequestDto.getAnswerContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

}