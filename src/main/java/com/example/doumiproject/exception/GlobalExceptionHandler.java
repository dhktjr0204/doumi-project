package com.example.doumiproject.exception;

import com.example.doumiproject.exception.api.PageNegativeValueException;
import com.example.doumiproject.exception.comment.EmptyCommentContentException;
import com.example.doumiproject.exception.comment.OverCommentLengthLimitException;
import com.example.doumiproject.exception.post.EmptyContentException;
import com.example.doumiproject.exception.post.EmptyTitleException;
import com.example.doumiproject.exception.quiz.OverAnswerLengthLimitException;
import com.example.doumiproject.exception.post.OverContentLengthLimitException;
import com.example.doumiproject.exception.post.NoContentException;
import com.example.doumiproject.exception.post.OverTitleLengthLimitException;
import com.example.doumiproject.exception.user.NotValidateUserException;
import com.example.doumiproject.exception.user.UserDuplicateException;
import com.example.doumiproject.exception.user.UserIdMismatchException;
import com.example.doumiproject.exception.user.UserLoginFailedException;
import com.example.doumiproject.exception.user.UserPwMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
        return new ErrorForm("아이디는 5글자 이상이며 영문자와 숫자만 가능합니다.", HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(UserPwMismatchException.class)
    public ErrorForm UserPwMismatchException(UserPwMismatchException ex) {
        // 개발자에게 알려줄 수 있는 수단 필요
        return new ErrorForm("비밀번호는 최소 8자 이상 최대 20자 이하, 하나 이상의 대문자, " +
            "하나의 소문자, 하나의 숫자, 하나의 특수 문자를 포함해야 합니다.", HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(UserLoginFailedException.class)
    public ResponseEntity<ErrorForm> UserLoginFailedException(UserLoginFailedException ex) {
        ErrorForm errorForm = new ErrorForm("아이디 또는 비밀번호가 일치하지 않습니다.",
            HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorForm);
    }

    @ExceptionHandler(NotValidateUserException.class)
    public ResponseEntity<String> NotValidateUserException(NotValidateUserException ex){
        return new ResponseEntity<>("인증되지 않은 사용자입니다.",HttpStatus.UNAUTHORIZED);
    }

    // User 관련 예외 끝

    // Quiz 관련 예외 시작
    @ExceptionHandler(EmptyTitleException.class)
    public ResponseEntity<String> EmptyTitleException(EmptyTitleException ex) {
        return new ResponseEntity<>("제목이 비어있습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyContentException.class)
    public ResponseEntity<String> EmptyContentException(EmptyContentException ex) {
        return new ResponseEntity<>("본문이 비어있습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OverTitleLengthLimitException.class)
    public ResponseEntity<String> OverTitleLengthLimitException(OverTitleLengthLimitException ex) {
        return new ResponseEntity<>("제목 길이가 최대 길이를 초과하였습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OverContentLengthLimitException.class)
    public ResponseEntity<String> OverContentLengthLimitException(OverContentLengthLimitException ex) {
        return new ResponseEntity<>("본문 길이가 최대 길이를 초과하였습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OverAnswerLengthLimitException.class)
    public ResponseEntity<String> QuizAnswerLengthException(OverAnswerLengthLimitException ex) {
        return new ResponseEntity<>("정답 길이가 최대 길이를 초과하였습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoContentException.class)
    public ResponseEntity<String>NoContentException(NoContentException ex, Model model){
        return ResponseEntity.notFound().build();
    }
    // Quiz 관련 예외 끝

    //comment 예외
    @ExceptionHandler(OverCommentLengthLimitException.class)
    public ResponseEntity<String> OverCommentLengthLimitException(OverCommentLengthLimitException ex){
        return new ResponseEntity<>("댓글 길이가 최대 길이를 초과하였습니다.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmptyCommentContentException.class)
    public ResponseEntity<String> EmptyCommentContentException(EmptyCommentContentException ex){
        return new ResponseEntity<>("댓글 내용이 비어있습니다.", HttpStatus.BAD_REQUEST);
    }


}
