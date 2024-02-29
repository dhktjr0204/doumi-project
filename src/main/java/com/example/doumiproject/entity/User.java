package com.example.doumiproject.entity;

import com.example.doumiproject.exception.user.UserIdMismatchException;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

@Data
public class User {

    private long id;
    private String userId;
    private String password;
    private String role;

    public User(String id, String password) {

        String IDREGEXP = "/^[a-zA-Z0-9]{5,}$/";
        String PWREGEXP = "/^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$/";

//        if(!Pattern.matches(IDREGEXP, id)) {
//            throw new UserIdMismatchException();
//        }
//        if(!Pattern.matches(PWREGEXP, password)) {
//            throw new UserIdMismatchException();
//        }

        this.userId = id;
        this.password = password;
        this.role = "USER";
    }
}
