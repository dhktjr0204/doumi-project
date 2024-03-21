package com.example.doumiproject.service;

public interface LikeService {

    public void addLike(long user_id, long post_id, String type);
    public void cancelLike(long user_id, long post_id, String type);
    public long getCountLike(long post_id, String type);
    public boolean existsByUserIdAndPostId(long user_id, long post_id);
}
