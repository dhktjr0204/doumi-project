package com.example.doumiproject.controller;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.entity.User;
import com.example.doumiproject.service.UserService;

import com.example.doumiproject.validate.UserValidator;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/signup")
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
    public String logout(HttpSession session) {
        session.invalidate(); //세션 무효화
        return "redirect:/";
    }

    @GetMapping("/user/mypage")
    public String myPage(HttpSession session, Model model) {

        return "mypage";
    }

    @GetMapping("/user/codingtest/post")
    public String getCodingTestPost(Model model, HttpSession session) {
        long userId = (long) session.getAttribute("userId");

        List<PostDto> userCoteList = userService.getAllUserCodingTestPosts(userId);

        model.addAttribute("coteList", userCoteList);

        return "mypageCodingTest";
    }

    @GetMapping("/user/quiz/post")
    public String getQuizPost(Model model, HttpSession session) {
        long userId = (long) session.getAttribute("userId");

        List<PostDto> userQuizList = userService.getAllUserQuizPosts(userId);

        model.addAttribute("quizList", userQuizList);

        return "mypageQuiz";
    }

    @GetMapping("/user/comment/post")
    public String getCommentPost(Model model, HttpSession session) {
        long userId = (long) session.getAttribute("userId");

        List<CommentDto> userCommentList = userService.getAllUserCommentPosts(userId);

        model.addAttribute("commentList", userCommentList);

        return "mypageComment";
    }
}
