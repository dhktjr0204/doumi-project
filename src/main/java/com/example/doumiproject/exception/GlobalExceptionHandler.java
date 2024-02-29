package com.example.doumiproject.exception;

import com.example.doumiproject.exception.api.PageNegativeValueException;
import com.example.doumiproject.exception.quiz.QuizAnswerLengthException;
import com.example.doumiproject.exception.quiz.QuizContentsLengthException;
import com.example.doumiproject.exception.quiz.QuizTitleLengthException;
import com.example.doumiproject.exception.user.UserDuplicateException;
import com.example.doumiproject.exception.user.UserIdMismatchException;
import com.example.doumiproject.exception.user.UserPwMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Rest Api를 관련 예외 시작
    @ExceptionHandler(PageNegativeValueException.class)
    public ErrorForm PageNegativeValueException(PageNegativeValueException ex) {
        // 개발자에게 알려줄 수 있는 수단 필요
        return new ErrorForm("페이지 값이 음수입니다.", HttpStatus.BAD_REQUEST.value());
    }
    // Rest Api를 관련 예외 끝

    // User 관련 예외 시작
    @ExceptionHandler(UserDuplicateException.class)
    public ErrorForm DuplicateUserException(UserDuplicateException ex) {
        // 개발자에게 알려줄 수 있는 수단 필요
        return new ErrorForm("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(UserIdMismatchException.class)
    public ErrorForm UserIdMismatchException(UserIdMismatchException ex) {
        // 개발자에게 알려줄 수 있는 수단 필요
        return new ErrorForm("닉네임은 5글자 이상이며 영문자와 숫자만 가능합니다.", HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(UserPwMismatchException.class)
    public ErrorForm UserPwMismatchException(UserPwMismatchException ex) {
        // 개발자에게 알려줄 수 있는 수단 필요
        return new ErrorForm("비밀번호는 최소 8자 이상 최대 20자 이하, 하나 이상의 대문자, " +
                "하나의 소문자, 하나의 숫자, 하나의 특수 문자를 포함해야 합니다.", HttpStatus.BAD_REQUEST.value());
    }
    // User 관련 예외 끝
    
    // Quiz 관련 예외 시작
    @ExceptionHandler(QuizTitleLengthException.class)
    public ErrorForm QuizTitleLengthException(QuizTitleLengthException ex) {
        return new ErrorForm("퀴즈 타이틀 길이가 적합하지 않습니다", HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(QuizContentsLengthException.class)
    public ErrorForm QuizContentsLengthException(QuizContentsLengthException ex) {
        return new ErrorForm("퀴즈 내용 길이가 적합하지 않습니다", HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(QuizAnswerLengthException.class)
    public ErrorForm QuizAnswerLengthException(QuizAnswerLengthException ex) {
        return new ErrorForm("퀴즈 정답 길이가 적합하지 않습니다", HttpStatus.BAD_REQUEST.value());
    }
    // Quiz 관련 예외 끝

   
}