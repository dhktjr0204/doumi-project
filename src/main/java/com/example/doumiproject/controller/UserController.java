package com.example.doumiproject.controller;

import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.entity.User;
import com.example.doumiproject.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user/signup")
    //ResponseEntity는 스프링의 HTTP 응답을 나타내는 클래스다.이 클래스를 사용해 json응답을 반환한다
    //fetch를 통해 전송된 JSON을  RequestBody를 통해 받는다
    public ResponseEntity<?> save(@Valid @RequestBody Map<String, String> userData) {
        //SignUpDto를 사용하지 않고 직접 User객체로 값을 넣는다
        //UserService로 User를 회원가입시킨다
        userService.join(userData.get("id"), userData.get("password"));

        Map<String, Object> response = new HashMap<>() {{
            put("success", true);
            put("message", "회원가입 성공!");
        }};

        return ResponseEntity.ok(response);
//        try {
//            userService.join(userData.get("id"), userData.get("password"));
//            Map<String, Object> response = new HashMap<>();
//            response.put("success", true);
//            response.put("message", "회원가입 성공!");
//            return ResponseEntity.ok(response);
//        } catch (IllegalStateException e) {
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("message", e.getMessage());
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
//        }
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
//            session.setAttribute("loginUser",
//                user); //session에 loginUser라는 이름의 user 객체를 담는다 (로그인 회원 정보 보관)
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


    //    수정이 필요한 코드
    @GetMapping("/user/codingtest/post")
    @ResponseBody
    public String userCotePost(Model model) {
        List<PostDto> postList = new ArrayList<>();
        PostDto testPost = new PostDto();
        testPost.setId(1);
        testPost.setTitle("테스트1");
        testPost.setLikeCount(1);
        postList.add(testPost);

        model.addAttribute("cote", postList);
        return "mypage";
    }
}
