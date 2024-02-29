package com.example.doumiproject.service;

import com.example.doumiproject.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;

    @Override
    public void addLike(long user_id, long post_id) {

        likeRepository.addLike(user_id, post_id);
    }

    @Override
    public void cancelLike(long user_id, long post_id) {

        likeRepository.cancelLike(user_id, post_id);
    }

    @Override
    public long getCountLike(long post_id) {

        return likeRepository.countLike(post_id);
    }

    @Override
    public boolean existsByUserIdAndPostId(long user_id, long post_id) {

        return likeRepository.existsByUserIdAndPostId(user_id, post_id);
    }
}
