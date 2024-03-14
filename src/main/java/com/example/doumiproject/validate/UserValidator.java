package com.example.doumiproject.validate;

import com.example.doumiproject.entity.User;
import com.example.doumiproject.exception.user.UserIdMismatchException;
import com.example.doumiproject.exception.user.UserPwMismatchException;
import java.util.regex.Pattern;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class UserValidator implements Validator {

    //아이디,비밀번호 정규식 , 테스트용 정규식이라 나중에 바꿔야합니다
    private static final String ID_REGEEXP = "^[a-zA-Z0-9]{4,20}$";
    private static final String PW_REGEXP = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{4,20}$";

    @Override
    public boolean supports(Class<?> clazz) {//검증하려는 클래스를 체크하는 메소드
        return clazz.isAssignableFrom(User.class);
    }

    @Override
    public void validate(Object target, Errors errors) {//실제 검증하려는 로직
        User user = (User) target;

        isNull(user);
        isMatch(user);
    }

    private void isNull(User user) {
        if (user.getUserId() == null) {
            throw new UserIdMismatchException();
        }

        if (user.getPassword() == null) {
            throw new UserPwMismatchException();
        }
    }

    private void isMatch(User user) {
        if (!isPatternMatch(ID_REGEEXP, user.getUserId())) {
            throw new UserIdMismatchException();
        }

        if (!isPatternMatch(PW_REGEXP, user.getPassword())) {
            throw new UserPwMismatchException();
        }
    }

    private boolean isPatternMatch(String regex, String input) {
        return Pattern.matches(regex, input);
    }
}
