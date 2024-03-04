package com.example.doumiproject.config;

import com.example.doumiproject.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {//인터셉터는 Spring MVC에 등록한다

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
            .addPathPatterns("/**")//모든 요청에 대해서 인터셉터를 적용한다
            .excludePathPatterns("/user/login", "/user/signup", "/"
                , "/Js/**", "/css/**", "/images/**", "/error",
                "/favicon.ico"); //제외할 경로를 설정 , css Js를 따로 적어주지 않으면 css,JS가 적용이 안된다
    }

}
