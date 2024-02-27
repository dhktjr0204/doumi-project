package com.example.doumiproject.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class LikeRepositoryTest {

    @Autowired
    LikeRepository likeRepository;

    @Test
    @DisplayName("좋아요 insert 테스트")
    public void addLikeTest() {

        for(int i=1; i<=10; i++) {
            for(int j=1; j<=10; j++) {

                likeRepository.addLike(i, j);
            }
        }
    }

    @Test
    @DisplayName("좋아요 delete 테스트")
    public void cancelLikeTest() {

        likeRepository.addLike(1,1);
        likeRepository.addLike(1,2);
        likeRepository.cancelLike(1,1);
    }

    @Test
    @DisplayName("좋아요 카운트 테스트")
    public void countLikeTest() {

        long post_id = 1;
        System.out.println(likeRepository.countLike(post_id));
    }
}
