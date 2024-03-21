package com.example.doumiproject.repository;

import com.example.doumiproject.dto.LikesDto;


public interface LikeRepository {

    public void addLike(long user_id, long post_id, String type);
    public void cancelLike(long user_id, long post_id, String type);
    public long countLike(long post_id, String type);
    public boolean existsByUserIdAndPostId(long user_id, long post_id);
}
