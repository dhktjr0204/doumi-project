package com.example.doumiproject.controller;

import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.entity.User;
import com.example.doumiproject.repository.JdbcTemplatePostRepository;
import com.example.doumiproject.service.QuizService;
import com.example.doumiproject.service.UserService;

import com.example.doumiproject.util.PaginationUtil;
import com.example.doumiproject.validate.UserValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Tag(name = "User", description = "User Controller")
public class UserController {

    private final UserService userService;
    private final JdbcTemplatePostRepository jdbcTemplatePostRepository;


    public UserController(UserService userService,
        JdbcTemplatePostRepository jdbcTemplatePostRepository) {
        this.userService = userService;
        this.jdbcTemplatePostRepository = jdbcTemplatePostRepository;
    }


    @GetMapping("/user/login")
    @Operation(summary = "로그인 페이지를 조회할 수 있는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String login() {
        return "login";
    }

    @GetMapping("/user/signup")
    @Operation(summary = "회원가입 페이지를 조회할 수 있는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String signUp() {
        return "signUp";
    }

    @PostMapping("/user/signup")
    @Operation(summary = "회원가입 API", description = "DB에 회원의 정보를 저장합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "회원가입 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"success\": true, \"message\": \"회원가입 성공!\"}"))),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 id,password 입력 값",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "아이디 오류", value = "{\"errormsg\": \"아이디는 5글자 이상이며 영문자와 숫자만 가능합니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "비밀번호 오류", value = "{\"errormsg\": \"비밀번호는 최소 8자 이상 최대 20자 이하, 하나 이상의 대문자,하나의 소문자, 하나의 숫자, 하나의 특수 문자를 포함해야 합니다.\", \"errorcode\":400}")
                }))
    })
    public ResponseEntity<?> save(@RequestBody User user, BindingResult bindingResult) {

        UserValidator userValidator = new UserValidator();
        userValidator.validate(user, bindingResult);
        userService.join(user.getUserId(), user.getPassword());

        Map<String, Object> response = new HashMap<>() {{
            put("success", true);
            put("message", "회원가입 성공!");
        }};

        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/login")
    @Operation(summary = "로그인 API", description = "회원 정보를 조회한 후 ,세션에 회원 정보를 저장하고 세션 유지 시간 설정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(value = "{\"success\": true, \"message\": \"로그인 성공!\"}"))),
        @ApiResponse(responseCode = "409", description = "잘못된 요청 id,password 입력 값",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "로그인 오류", value = "{\"errormsg\": \"아이디 또는 비밀번호가 일치하지 않습니다.\", \"errorcode\":409}")
                }))
    })
    public ResponseEntity<?> login(HttpServletRequest request) {
        //1.회원 정보 조회
        String loginId = request.getParameter("id");
        String loginPassword = request.getParameter("password");
        User user = userService.login(loginId, loginPassword);

        //2. 세션에 회원 정보를 저장 & 세션 유지 시간 설정
        //request에 세션이 있으면 세션을 반환하고, 없으면 신규 세션을 생성해 session에 담는다.
        HttpSession session = request.getSession();
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getUserId());
        session.setMaxInactiveInterval(1800);

        //3. 로그인 성공 응답 반환
        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("success", true);
        successResponse.put("message", "로그인 성공!");
        return ResponseEntity.ok(successResponse);
    }

    @GetMapping("/user/logout")
    @Operation(summary = "로그아웃 API", description = "회원의 세션을 무효화합니다. 메인 페이지로 돌아갑니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그아웃시 메인 페이지 HTML 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String logout(HttpSession session) {
        session.invalidate(); //세션 무효화
        return "redirect:/";
    }

    @GetMapping("/user/{userId}/mypage")
    @Operation(summary = "마이페이지 조회 API", description = "사용자의 마이페이지를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "마이페이지 HTML 페이지 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String myPage(@PathVariable("userId") Long userId, HttpSession session, Model model) {

        return "myPage/myPage";
    }

    @GetMapping("/user/{userId}/codingtest/posts")
    @Operation(summary = "유저가 작성한 코딩테스트 질문 글 목록 조회 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저가 작성한 코딩 테스트 글 목록 데이터와 함께 HTML 페이지 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String getCodingTestPost(@PathVariable("userId") Long userId, Model model,
        HttpSession session) {

        List<PostDto> userCoteList = jdbcTemplatePostRepository.findAllUserCodingTestPosts(userId);

        model.addAttribute("coteList", userCoteList);

        return "myPage/myPageCodingTest";
    }

    @GetMapping("/user/{userId}/quiz/posts")
    @Operation(summary = "유저가 작성한 퀴즈 글 목록 조회 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저가 작성한 퀴즈 글 목록 데이터와 함께 HTML 페이지 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String getQuizPost(@PathVariable("userId") Long userId, Model model,
        HttpSession session) {

        List<PostDto> userQuizList = jdbcTemplatePostRepository.findAllUserQuizPosts(userId);

        model.addAttribute("quizList", userQuizList);

        return "myPage/myPageQuiz";
    }

    @GetMapping("/user/{userId}/comment/posts")
    @Operation(summary = "유저가 작성한 댓글 목록 조회 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저가 작성한 댓글 목록 데이터와 함께 HTML 페이지 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String getCommentPost(@PathVariable("userId") Long userId, Model model,
        HttpSession session) {

        List<Comment> userCommentList = userService.getAllUserCommentPosts(userId);

        model.addAttribute("commentList", userCommentList);

        return "myPage/myPageComment";
    }

//    private void setPaginationAttributes(Model model, int page, int totalPages, List<PostDto> quizs) {
//
//        int startIdx = PaginationUtil.calculateStartIndex(page);
//        int endIdx = PaginationUtil.calculateEndIndex(page, totalPages);
//
//        model.addAttribute("quizs", quizs);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("startIdx", startIdx);
//        model.addAttribute("endIdx", endIdx);
//        model.addAttribute("totalPages", totalPages);
//    }

//    private void setPaginationAttributes(Model model, int page, int totalPages, List<PostDto> cotes) {
//
//        int startIdx = PaginationUtil.calculateStartIndex(page);
//        int endIdx = PaginationUtil.calculateEndIndex(page, totalPages);
//
//        model.addAttribute("cotes", cotes);
//        model.addAttribute("currentPage", page);
//        model.addAttribute("startIdx", startIdx);
//        model.addAttribute("endIdx", endIdx);
//        model.addAttribute("totalPages", totalPages);
//    }
}
