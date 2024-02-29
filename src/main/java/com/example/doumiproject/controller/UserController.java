package com.example.doumiproject.controller;

import com.example.doumiproject.entity.User;
import com.example.doumiproject.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    //ResponseEntity는 스프링의 HTTP 응답을 나타내는 클래스다.이 클래스를 사용해 json응답을 반환한다
    //fetch를 통해 전송된 JSON을  RequestBody를 통해 받는다
    public ResponseEntity<?> save(@RequestBody Map<String, String> userData) {
        //SignUpDto를 사용하지 않고 직접 User객체로 값을 넣는다
        //UserService로 User를 회원가입시킨다
        try {
            userService.join(userData.get("id"), userData.get("password"));
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "회원가입 성공!");
            return ResponseEntity.ok(successResponse);
        } catch (IllegalStateException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> login(HttpServletRequest request) {
        try {
            //1.회원 정보 조회
            String loginId = request.getParameter("id");
            String loginPassword = request.getParameter("password");
            User user = userService.login(loginId, loginPassword);

            //2. 세션에 회원 정보를 저장 & 세션 유지 시간 설정
            //request에 세션이 있으면 세션을 반환하고, 없으면 신규 세션을 생성해 session에 담는다.
            HttpSession session = request.getSession();
            session.setAttribute("loginUser",
                user); //session에 loginUser라는 이름의 user 객체를 담는다 (로그인 회원 정보 보관)
            session.setMaxInactiveInterval(1800);

            //3. 로그인 성공 응답 반환
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("success", true);
            successResponse.put("message", "로그인 성공!");
            return ResponseEntity.ok(successResponse);

        } catch (IllegalArgumentException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @GetMapping("/user/logout")
    public String logout(HttpSession session) {
        session.invalidate(); //세션 무효화
        return "redirect:/";
    }
}
