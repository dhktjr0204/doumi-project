package com.example.doumiproject.service;

import com.example.doumiproject.entity.User;
import com.example.doumiproject.exception.user.UserDuplicateException;
import com.example.doumiproject.repository.UserRepository;
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
        String encryptedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        User user = new User(userId, encryptedPassword);

        //중복 회원 검증
//        validateDuplicateUser(user);

        //중복 회원이 아니면 userRepository에 저장한다
        userRepository.save(user);
    }

    /*중복 회원 검증*/
//     private void validateDuplicateUser(User user) {
// <<<<<<< develop
//         User result = userRepository.findByUserId(user.getUserId());
//         if (result != null) {//사용자를 찾았다면, 즉 중복 아이디가 있다면
//             throw new IllegalStateException("이미 존재하는 아이디입니다.");
// =======
//         boolean exists;
//         try {
//             exists = userRepository.existsByUserId(user.getUserId());
//         }
//         catch (Exception ex) {
//             throw new UserDuplicateException();
//         }
//         if (exists) {
//             throw new UserDuplicateException();
// >>>>>>> develop
//         }
//     }

    /*로그인*/
    public User login(String userId, String password) {
        User user = userRepository.findByUserId(userId);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        } else {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
    }
}
