package com.example.doumiproject.controller;

import com.example.doumiproject.dto.*;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.exception.user.NotValidateUserException;
import com.example.doumiproject.service.CommentService;
import com.example.doumiproject.service.QuizService;
import com.example.doumiproject.util.PaginationUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import com.example.doumiproject.validate.QuizValidator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/quiz")
@Tag(name = "Quiz", description = "Quiz Controller")
public class QuizController {

    private final QuizService quizService;
    private final CommentService commentService;

    private int pageSize = 10;

    @GetMapping("")
    @Operation(summary = "퀴즈 메인페이지 조회 API", description = "퀴즈 메인페이지를 조회하는 API며, 페이징을 포함합니다."
        + "query로 page 번호를 줄 수 있으며 기본값은 1입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "퀴즈 메인페이지 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String index(@RequestParam(defaultValue = "1") int page, Model model) {

        if (page < 1) {
            page = 1;
        }

        setPaginationAttributes(model, page,
            quizService.getTotalPages(pageSize), quizService.getAllQuiz(page, pageSize));

        return "quiz/index";
    }

    @GetMapping("/search")
    @Operation(summary = "퀴즈의 제목과 내용을 검색한 결과를 조회하는 API", description = "퀴즈 제목,내용 검색 결과를 조회하는 API며, "
        + "query로 keyword와 page 번호를 줄 수 있으며 page 기본값은 1입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "퀴즈 검색 결과 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String search(@RequestParam(value = "keyword") String keyword,
        @RequestParam(defaultValue = "1", value = "page") int page, Model model) {

        if (page < 1) {
            page = 1;
        }

        setPaginationAttributes(model, page,
            quizService.getTotalPagesForSearch(pageSize, keyword),
            quizService.getSearchQuiz(keyword, page, pageSize));
        model.addAttribute("keyword", keyword);

        return "quiz/search";
    }

    @GetMapping("/tag")
    @Operation(summary = "특정 태그와 관련된 퀴즈 게시글들을 조회하는 API", description =
        "특정 태그와 관련된 퀴즈 게시글을 조회하는  API며, "
            + "query로 태그명인 name과 page 번호를 줄 수 있으며 page 기본값은 1입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "퀴즈 태그 검색 결과 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String searchTags(@RequestParam(value = "name") String tag,
        @RequestParam(defaultValue = "1", value = "page") int page, Model model) {

        if (page < 1) {
            page = 1;
        }

        setPaginationAttributes(model, page,
            quizService.getTotalPagesForSelectedTag(pageSize, tag),
            quizService.getQuizForSelectedTag(tag, page, pageSize));
        model.addAttribute("tag", tag);

        return "quiz/tag";
    }

    private void setPaginationAttributes(Model model, int page, int totalPages,
        List<PostDto> quizs) {

        int startIdx = PaginationUtil.calculateStartIndex(page);
        int endIdx = PaginationUtil.calculateEndIndex(page, totalPages);

        model.addAttribute("quizs", quizs);
        model.addAttribute("currentPage", page);
        model.addAttribute("startIdx", startIdx);
        model.addAttribute("endIdx", endIdx);
        model.addAttribute("totalPages", totalPages);
    }

    @GetMapping("/board")
    @Operation(summary = "퀴즈 게시글을 조회할 수 있는 API", description = "특정 퀴즈 게시글을 조회할 수 있는 API로 query로 게시글의 id값을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "특정 퀴즈 게시글을 조회한 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String getQuizDetail(@RequestParam("id") Long id, Model model, HttpSession session) {

        long userId = (long) session.getAttribute("userId");

        //글의 상세 정보 가져오기
        QuizDto quiz = quizService.getQuiz(id, userId);
        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments = commentService.getAllComments(id, userId);

        model.addAttribute("quiz", quiz);
        model.addAttribute("postId", quiz.getId());
        model.addAttribute("comments", comments);
        model.addAttribute("newComment", new Comment("QUIZ"));

        return "quiz/board";
    }

    @GetMapping("/post")
    @Operation(summary = "퀴즈를 출제할 수 있는 페이지 조회 API", description = "유저가 퀴즈를 출제할 수 있는 페이지로 이동하는 API입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "퀴즈 출제 form HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String getPostQuizForm(Model model) {

        //타입별 태그 모두 불러오기
        List<TagDto> tags = quizService.getAllTags();

        model.addAttribute("tags", tags);
        model.addAttribute("quiz", new QuizRequestDto());

        return "quiz/form";
    }

    @PostMapping("/post")
    @Operation(summary = "퀴즈를 출제하는 API", description = "유저가 퀴즈를 등록하면 내용들이 DB에 저장되고 자신이 출제한 퀴즈 게시글로 이동합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저가 출제한 퀴즈 게시글 HTML을 반환",
            content = @Content(mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 입력 값",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "퀴즈 제목 입력 오류", value = "{\"errormsg\": \"제목이 비어있습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "퀴즈 제목 입력 오류", value = "{\"errormsg\": \"제목 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "퀴즈 본문 입력 오류", value = "{\"errormsg\": \"본문이 비어있습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "퀴즈 본문 입력 오류", value = "{\"errormsg\": \"본문 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "정답 입력 오류", value = "{\"errormsg\": \"정답 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}")
                }))
    })
    public ResponseEntity<String> postQuiz(QuizRequestDto quiz, BindingResult result,
        HttpSession session) {

        validateQuiz(quiz, result);

        long userId = (long) session.getAttribute("userId");

        Long postId = quizService.saveQuiz(quiz, userId);

        return ResponseEntity.ok("/quiz/board?id=" + postId);
    }

    @GetMapping("/edit")
    @Operation(summary = "퀴즈를 수정할 수 있는 페이지 조회 API", description = "유저가 등록한 퀴즈 내용을 수정할 수 있는 페이지를 조회하는 API로 query로 게시글의 id값을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "퀴즈 수정 form HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String getEditQuizForm(@RequestParam("id") Long id, Model model, HttpSession session) {
        long userId = (long) session.getAttribute("userId");

        QuizDto quiz = quizService.getQuiz(id, userId);
        List<TagDto> tags = quizService.getAllTags();

        //현재 로그인 된 유저가 글쓴 유저가 아닐 경우 예외처리
        if (userId != quiz.getUserId()) {
            return "error/404";
        }

        model.addAttribute("quiz", quiz);
        model.addAttribute("postId", quiz.getId());
        model.addAttribute("tags", tags);

        return "quiz/edit";
    }

    @PutMapping("/edit")
    @Operation(summary = "퀴즈를 수정할 수 있는 API", description = "유저가 등록한 퀴즈 내용을 수정할 수 있는 API로 query로 게시글의 id값을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정된 퀴즈 게시글 HTML을 반환",
            content = @Content(mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 입력 값",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "퀴즈 제목 입력 오류", value = "{\"errormsg\": \"제목이 비어있습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "퀴즈 제목 입력 오류", value = "{\"errormsg\": \"제목 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "퀴즈 본문 입력 오류", value = "{\"errormsg\": \"본문이 비어있습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "퀴즈 본문 입력 오류", value = "{\"errormsg\": \"본문 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "정답 입력 오류", value = "{\"errormsg\": \"정답 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}")
                })),
        @ApiResponse(responseCode = "401",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(name = "사용자 인증 오류", value = "{\"errormsg\": \"인증되지 않은 사용자입니다.\", \"errorcode\":401}")))
    })
    public ResponseEntity<String> updateQuiz(@RequestParam("id") Long id, QuizRequestDto quiz,
        BindingResult result, HttpSession session) {

        validateQuiz(quiz, result);

        long userId = (long) session.getAttribute("userId");

        //현재 로그인 된 유저가 글쓴 유저가 아닐 경우 예외처리
        if (quiz.getUserId() != userId) {
            throw new NotValidateUserException();
        }

        quizService.updateQuiz(quiz, id);

        return ResponseEntity.ok("/quiz/board?id=" + id);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "퀴즈를 삭제할 수 있는 API", description = "유저가 등록한 퀴즈를 삭제하는 API로 query로 게시글의 id값을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "퀴즈 메인페이지 HTML을 반환 ",
            content = @Content(mediaType = "text/html")),
        @ApiResponse(responseCode = "401",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(name = "사용자 인증 오류", value = "{\"errormsg\": \"인증되지 않은 사용자입니다.\", \"errorcode\":401}")))
    })
    public ResponseEntity<String> deleteQuiz(@RequestParam("postId") long postId,
        @RequestParam("userId") long author, HttpSession session) {

        long userId = (long) session.getAttribute("userId");

        if (author != userId) {
            throw new NotValidateUserException();
        }

        quizService.deleteQuiz(postId);

        return ResponseEntity.ok("/quiz");
    }

    private void validateQuiz(QuizRequestDto quiz, BindingResult result) {
        QuizValidator quizValidator = new QuizValidator();
        quizValidator.validate(quiz, result);
    }
}
