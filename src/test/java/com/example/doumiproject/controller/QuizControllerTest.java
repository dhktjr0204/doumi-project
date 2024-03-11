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
    public void setup() throws Exception {
        session = new MockHttpSession();
        // 테스트 전에 세션에 userId와 userName 저장
        session.setAttribute("userId", TEST_USER_ID);
        session.setAttribute("userName", TEST_USER_NAME);
    }

    @Test
    @DisplayName("저장 테스트")
    void postQuiz() throws Exception {
        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("Sample Title")
                .quizContent("Sample quiz Content")
                .answerContent("Sample Anwser Content")
                .tags("1,2,3,4")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/quiz/post")
                        .session(session)
                        .param("userId", Integer.toString((int) quizRequestDto.getUserId()))
                        .param("title", quizRequestDto.getTitle())
                        .param("quizContent", quizRequestDto.getQuizContent())
                        .param("answerContent", quizRequestDto.getAnswerContent())
                        .param("tags", quizRequestDto.toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("수정 테스트")
    void editQuiz() throws Exception {
        long quizId = 1;

        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("Sample Title")
                .quizContent("Sample quiz Content")
                .answerContent("Sample Anwser Content")
                .tags("1,2,3,4")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/quiz/edit")
                        .session(session)
                        .param("id", String.valueOf(quizId))
                        .param("userId", Integer.toString((int) quizRequestDto.getUserId()))
                        .param("title", quizRequestDto.getTitle())
                        .param("quizContent", quizRequestDto.getQuizContent())
                        .param("answerContent", quizRequestDto.getAnswerContent())
                        .param("tags", quizRequestDto.toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("권한이 없는 유저가 업데이트 요청했을 때 테스트")
    void editQuiz_withValidUser() throws Exception {
        long quizId = 1;
        long userId = 2;

        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .userId(userId)
                .title("Sample Title")
                .quizContent("Sample quiz Content")
                .answerContent("Sample Anwser Content")
                .tags("1,2,3,4")
                .build();

        String expectedMessage = "인증되지 않은 사용자입니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/quiz/edit")
                        .session(session)
                        .param("id", String.valueOf(quizId))
                        .param("userId", Integer.toString((int) quizRequestDto.getUserId()))
                        .param("title", quizRequestDto.getTitle())
                        .param("quizContent", quizRequestDto.getQuizContent())
                        .param("answerContent", quizRequestDto.getAnswerContent())
                        .param("tags", quizRequestDto.toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteQuiz() throws Exception {
        long quizId = 1;
        long userId = 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/quiz/delete")
                        .param("postId", String.valueOf(quizId))
                        .param("userId", String.valueOf(userId))
                        .session(session)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("권한이 없는 유저가 삭제 요청을 했을 때 테스트")
    void deleteQuiz_withvalidUser() throws Exception {
        long quizId = 1;
        long userId = 2;

        String expectedMessage = "인증되지 않은 사용자입니다.";

        mockMvc.perform(MockMvcRequestBuilders.delete("/quiz/delete")
                        .param("postId", String.valueOf(quizId))
                        .param("userId", String.valueOf(userId))
                        .session(session)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }


    @Test
    @DisplayName("contents가 3000자 이상일 때 테스트")
    void postQuiz_withInvalidQuizCountentLength() throws Exception {

        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("Sample Title")
                .quizContent("Sample quiz content with more than 3000 characters. Sample quiz content with more than 3000 characters.\n".repeat(40))
                .answerContent("Sample Anwser Content")
                .build();

        String expectedMessage = "본문 길이가 최대 길이를 초과하였습니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/quiz/post")
                        .session(session)
                        .param("userId", Integer.toString((int) quizRequestDto.getUserId()))
                        .param("title", quizRequestDto.getTitle())
                        .param("quizContent", quizRequestDto.getQuizContent())
                        .param("answerContent", quizRequestDto.getAnswerContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("title이 빈 칸 일때 테스트")
    void postQuiz_withValidTitleNull() throws Exception {
        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("")
                .quizContent("Sample quiz Content")
                .answerContent("Sample Anwser Content")
                .build();

        String expectedMessage = "제목이 비어있습니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/quiz/post")
                        .session(session)
                        .param("userId", Integer.toString((int) quizRequestDto.getUserId()))
                        .param("title", quizRequestDto.getTitle())
                        .param("quizContent", quizRequestDto.getQuizContent())
                        .param("answerContent", quizRequestDto.getAnswerContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));

    }

    @Test
    @DisplayName("title이 250자 이상 일 때 테스트")
    void postQuiz_withValidTitleLength() throws Exception {
        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요안녕하세요")
                .quizContent("Sample quiz content with more than 3000 characters. Sample quiz content with more than 3000 characters.\n".repeat(40))
                .answerContent("Sample Anwser Content")
                .build();

        String expectedMessage = "제목 길이가 최대 길이를 초과하였습니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/quiz/post")
                        .session(session)
                        .param("userId", Integer.toString((int) quizRequestDto.getUserId()))
                        .param("title", quizRequestDto.getTitle())
                        .param("quizContent", quizRequestDto.getQuizContent())
                        .param("answerContent", quizRequestDto.getAnswerContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("answer이 빈칸일 때 테스트")
    void postQuiz_withValidAnswerLength() throws Exception {
        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("Sample title")
                .quizContent("Sample quiz Content3000")
                .answerContent("")
                .build();
        mockMvc.perform(MockMvcRequestBuilders.post("/quiz/post")
                        .session(session)
                        .param("userId", Integer.toString((int) quizRequestDto.getUserId()))
                        .param("title", quizRequestDto.getTitle())
                        .param("quizContent", quizRequestDto.getQuizContent())
                        .param("answerContent", quizRequestDto.getAnswerContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("answer이 3000자 이상일 때 테스트")
    void postQuiz_withValidAnswerNull() throws Exception {
        QuizRequestDto quizRequestDto = QuizRequestDto.builder()
                .userId(TEST_USER_ID)
                .title("Sample title")
                .quizContent("Sample quiz Content3000")
                .answerContent("Sample quiz content with more than 3000 characters. Sample quiz content with more than 3000 characters.\n".repeat(40))
                .build();

        String expectedMessage = "정답 길이가 최대 길이를 초과하였습니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/quiz/post")
                        .session(session)
                        .param("userId", Integer.toString((int) quizRequestDto.getUserId()))
                        .param("title", quizRequestDto.getTitle())
                        .param("quizContent", quizRequestDto.getQuizContent())
                        .param("answerContent", quizRequestDto.getAnswerContent())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

}