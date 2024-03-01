package com.example.doumiproject.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QuizRepositoryTest {

    @Autowired
    QuizRepository quizRepository;

    @Test
    void getQuizDetailsTest() {

        long post_id = 110;
        long user_id = 1;

        System.out.println(quizRepository.getQuizDetails(post_id, user_id));
    }
}