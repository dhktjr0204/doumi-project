package com.example.doumiproject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LikesDto {

    private long id;
    private long user_id;
    private long post_id;
    private long likeCount;
    private boolean exists;
}
