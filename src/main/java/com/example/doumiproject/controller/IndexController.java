package com.example.doumiproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Tag(name = "Index", description = "Index Controller")
public class IndexController {

    @GetMapping("/")
    @Operation(summary = "메인페이지를 조회할 수 있는 API", description = "메인페이지 조회 API로 로그인 유무에 따라 보이는 화면이 달라집니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "메인페이지 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
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

}
