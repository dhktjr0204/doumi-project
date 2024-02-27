package com.example.doumiproject.repository;

public interface LikeRepository {

    public void addLike(long user_id, long post_id);
    public void cancelLike(long user_id, long post_id);
    public long countLike(long post_id);
}
