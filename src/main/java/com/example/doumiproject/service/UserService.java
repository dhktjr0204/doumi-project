package com.example.doumiproject.service;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.entity.User;
import com.example.doumiproject.exception.user.UserDuplicateException;
import com.example.doumiproject.exception.user.UserLoginFailedException;
import com.example.doumiproject.repository.UserRepository;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {//UserService 객체 생성 시
        this.userRepository = userRepository; //UserRepository 객체 생성
    }

    /*회원 가입*/

    public void join(String userId, String password) {
        //비밀번호 암호화
        String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt(10));

        User user = User.builder()
            .userId(userId)
            .password(encryptedPassword)
            .build();

        //중복 회원 검증
        validateDuplicateUser(userId);

        //중복 회원이 아니면 userRepository에 저장한다
        userRepository.save(user);
    }

    /*중복 회원 검증*/
    private void validateDuplicateUser(String userId) {
        User result;
        try {
            result = userRepository.findByUserId(userId);
        } catch (Exception ex) {
            throw new UserDuplicateException();
        }
        if (result != null) {
            throw new UserDuplicateException();
        }
    }

    /*로그인*/
    public User login(String userId, String password) {
        User user = userRepository.findByUserId(userId);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            throw new UserLoginFailedException();
        }
    }

    public List<Comment> getAllUserCommentPosts(Long userId) {
        List<Comment> userCommentPosts = userRepository.findAllUserCommentPosts(userId);
        return userCommentPosts;
    }

}
