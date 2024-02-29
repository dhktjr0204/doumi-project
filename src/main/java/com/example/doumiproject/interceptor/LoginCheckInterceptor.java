package com.example.doumiproject.interceptor;

import com.example.doumiproject.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {

        //1. 세션에서 회원 정보를 조회
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");

        //2. 회원 정보를 체크한다
        if (user == null) {//로그인 되어 있지 않은 상태라면
            response.sendRedirect("/user/login");//로그인 화면으로 보낸다
            return false;
        }

        return true;
    }

}
