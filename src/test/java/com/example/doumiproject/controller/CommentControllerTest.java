package com.example.doumiproject.controller;

import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.service.CommentService;
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

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(CommentController.class)
class CommentControllerTest {
    @Autowired
    private MockMvc mockMvc;

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
    void addComment() throws Exception {
        long postId = 1;

        Comment comment = Comment.builder()
                .userId(TEST_USER_ID)
                .postId(postId)
                .contents("sample comment Contents")
                .display(true)
                .parentCommentId(0)
                .type("QUIZ")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/comment/add")
                        .session(session)
                        .param("userId", String.valueOf(comment.getUserId()))
                        .param("postId", String.valueOf(comment.getPostId()))
                        .param("contents", comment.getContents())
                        .param("display", String.valueOf(comment.isDisplay()))
                        .param("parentCommentId", String.valueOf(comment.getParentCommentId()))
                        .param("type", comment.getType())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("댓글 길이가 1500자 초과할 때 테스트")
    void addComment_withValidCommentLength() throws Exception {
        long postId = 1;

        Comment comment = Comment.builder()
                .userId(TEST_USER_ID)
                .postId(postId)
                .contents("sample comment Contentssample comment Contentssample comment Contentssample comment Contentssample comment Contents".repeat(15))
                .display(true)
                .parentCommentId(0)
                .type("QUIZ")
                .build();

        String expectedMessage = "댓글 길이가 최대 길이를 초과하였습니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/comment/add")
                        .session(session)
                        .param("userId", String.valueOf(comment.getUserId()))
                        .param("postId", String.valueOf(comment.getPostId()))
                        .param("contents", comment.getContents())
                        .param("display", String.valueOf(comment.isDisplay()))
                        .param("parentCommentId", String.valueOf(comment.getParentCommentId()))
                        .param("type", comment.getType())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("댓글 내용이 비어 있을 때 테스트")
    void addComment_withValidEmptyComment() throws Exception {
        long postId = 1;

        Comment comment = Comment.builder()
                .userId(TEST_USER_ID)
                .postId(postId)
                .contents("")
                .display(true)
                .parentCommentId(0)
                .type("QUIZ")
                .build();

        String expectedMessage = "댓글 내용이 비어있습니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/comment/add")
                        .session(session)
                        .param("userId", String.valueOf(comment.getUserId()))
                        .param("postId", String.valueOf(comment.getPostId()))
                        .param("contents", comment.getContents())
                        .param("display", String.valueOf(comment.isDisplay()))
                        .param("parentCommentId", String.valueOf(comment.getParentCommentId()))
                        .param("type", comment.getType())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("권한이 없는 사용자가 수정 요청했을 때 테스트")
    void editComment_withValidUser() throws Exception {
        long postId = 1;
        long userId = 2;
        long commentId = 1;

        Comment comment = Comment.builder()
                .userId(userId)
                .postId(postId)
                .contents("sample comment")
                .display(true)
                .parentCommentId(0)
                .type("QUIZ")
                .build();

        String expectedMessage = "인증되지 않은 사용자입니다.";

        mockMvc.perform(MockMvcRequestBuilders.post("/comment/edit")
                        .session(session)
                        .param("id", String.valueOf(commentId))
                        .param("userId", String.valueOf(comment.getUserId()))
                        .param("postId", String.valueOf(comment.getPostId()))
                        .param("contents", comment.getContents())
                        .param("display", String.valueOf(comment.isDisplay()))
                        .param("parentCommentId", String.valueOf(comment.getParentCommentId()))
                        .param("type", comment.getType())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.content().string(expectedMessage));
    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteComment() throws Exception {
        long postId = 1;
        long commentId = 1;
        long userId = TEST_USER_ID;

        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/delete")
                        .session(session)
                        .param("postId", String.valueOf(postId))
                        .param("commentId", String.valueOf(commentId))
                        .param("userId", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("권한 없는 유저가 삭제 요청했을 때 테스트")
    void deleteComment_withValidUser() throws Exception {
        long postId = 1;
        long commentId = 1;
        long userId = 2;

        mockMvc.perform(MockMvcRequestBuilders.delete("/comment/delete")
                        .session(session)
                        .param("postId", String.valueOf(postId))
                        .param("commentId", String.valueOf(commentId))
                        .param("userId", String.valueOf(userId)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}