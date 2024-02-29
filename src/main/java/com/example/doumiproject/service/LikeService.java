package com.example.doumiproject.service;

public interface LikeService {

    public void addLike(long user_id, long post_id);
    public void cancelLike(long user_id, long post_id);
    public long getCountLike(long post_id);
    public boolean existsByUserIdAndPostId(long user_id, long post_id);
}
