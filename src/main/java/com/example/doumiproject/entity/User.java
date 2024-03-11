package com.example.doumiproject.entity;

import com.example.doumiproject.exception.user.UserIdMismatchException;
import com.example.doumiproject.exception.user.UserPwMismatchException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class User {

    private long id;

    private String userId;

    private String password;

    private String role;

}
