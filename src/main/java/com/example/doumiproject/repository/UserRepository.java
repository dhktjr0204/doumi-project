package com.example.doumiproject.repository;

import com.example.doumiproject.entity.User;

import java.util.List;

public interface UserRepository {

    User save(User user); //회원을 저장하면 회원이 반환되는 기능

    boolean existsByUserId(String nickName); //nickName으로 회원을 찾는 기능

    List<User> findAllUser(); //모든 회원 리스트를 반환하는 기능

}
