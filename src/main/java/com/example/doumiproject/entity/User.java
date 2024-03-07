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
public class User {

    private long id;

    @NotBlank(message = "아이디는 필수 입력 값입니다.")
    @Size(min = 4, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "아이디는 최소 4글자 이상 최대 20자 이하이며 영문자와 숫자만 가능합니다.")
    private String userId;

    @NotEmpty(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\\\d)[A-Za-z\\\\d]{4,20}$", message = "비밀번호는 최소 4자 이상 최대 20자 이하, 하나 이상의 문자와 하나의 숫자를 포함해야 합니다.")
    private String password;
    private String role;

//    public User(String id, String password) {
//
//        String ID_REGEXP = "^[a-zA-Z0-9]{5,}$";
//        String PW_REGEXP = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$";
//
//        if (!Pattern.matches(ID_REGEXP, id)) {
//            throw new UserIdMismatchException();
//        }
//        if (!Pattern.matches(PW_REGEXP, password)) {
//            throw new UserPwMismatchException();
//        }
//
//        this.userId = id;
//        this.password = password;
//        this.role = "USER";
//    }
}
