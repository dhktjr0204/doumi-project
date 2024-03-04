package com.example.doumiproject.entity;

import com.example.doumiproject.exception.user.UserIdMismatchException;
import com.example.doumiproject.exception.user.UserPwMismatchException;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

@Data
public class User {

    private long id;
    private String userId;
    private String password;
    private String role;

//    public User(String id, String password) {
//
//        String ID_REGEXP = "^[a-zA-Z0-9]{5,}$";
//        String PW_REGEXP = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
//
//        if(!Pattern.matches(ID_REGEXP, id)) {
//            throw new UserIdMismatchException();
//        }
//        if(!Pattern.matches(PW_REGEXP, password)) {
//            throw new UserPwMismatchException();
//        }
//
//        this.userId = id;
//        this.password = password;
//        this.role = "USER";
//    }
}
