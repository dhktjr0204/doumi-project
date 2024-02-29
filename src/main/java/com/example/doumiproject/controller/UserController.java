package com.example.doumiproject.controller;

import com.example.doumiproject.service.UserService;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
        userService.join(userData.get("id"), userData.get("password"));

        Map<String, Object> response = new HashMap<>(){{
            put("success", true);
            put("message", "회원가입 성공!");
        }};

        return ResponseEntity.ok(response);


    }
}
