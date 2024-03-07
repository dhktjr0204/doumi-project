package com.example.doumiproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        //세션에서 사용자의 로그인 유무를 확인한다
        Object loginUserId = session.getAttribute("userId");
        Object loginUserName = session.getAttribute("userName");
        if (loginUserId != null) {
            model.addAttribute("userId", loginUserId);
            model.addAttribute("userName", loginUserName);
        }

        return "index";
    }

    @GetMapping("/user/login")
    public String login() {
        return "login";
    }

    @GetMapping("/user/signup")
    public String signUp() {
        return "signUp";
    }
}
