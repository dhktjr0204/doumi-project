package com.example.doumiproject.service;

import com.example.doumiproject.repository.LikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService{

    private final LikeRepository likeRepository;

    @Override
    public void addLike(long user_id, long post_id, String type) {

        likeRepository.addLike(user_id, post_id, type);
    }

    @Override
    public void cancelLike(long user_id, long post_id, String type) {

        likeRepository.cancelLike(user_id, post_id, type);
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
