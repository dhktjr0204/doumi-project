package com.example.doumiproject.interceptor;

import com.example.doumiproject.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        String uri = request.getRequestURI();

        //메인페이지,로그인,회원 가입 페이지는 인터셉터에서 제외한다
        if (uri.equals("/") || uri.startsWith("/user/login") || uri.startsWith("/user/signup")) {
            return true;
        }

        //세션에서 회원 정보를 조회
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("userId") != null) {
            return true; //세션에 로그인 정보가 있으면 요청을 계속 진행시킨다
        } else {
            response.sendRedirect("/user/login"); //세션정보가 없다면 로그인 페이지로 리다이렉트
            return false; //요청 처리를 중단한다.
        }
    }

    @Override //뷰에 "유저이름"님 환영합니다를 쓰기 위해 작성
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {

        // 요청 처리 후, 뷰가 렌더링 되기 전 실행한다
        if (modelAndView != null && request.getSession(false) != null) {
            HttpSession session = request.getSession();
            if (session.getAttribute("userId") != null) {//세션이 있다면
                // 모델에 로그인 사용자 정보를 추가한다 , view에 전달할 값을 설정한다.
                modelAndView.addObject("userId", session.getAttribute("userId"));
                //userId만 보낼지 userName까지 보낼지 몰라서 일단 두개 다 보냅니다!
                modelAndView.addObject("userName", session.getAttribute("userName"));
            }
        }
    }

}
