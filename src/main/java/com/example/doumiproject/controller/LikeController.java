package com.example.doumiproject.controller;

import com.example.doumiproject.dto.LikesDto;
import com.example.doumiproject.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @ResponseBody
    @GetMapping("")
    public ResponseEntity<?> getLikeInfo(@RequestParam(value = "user_id") long user_id,
                                         @RequestParam(value = "post_id") long post_id) {

        try {
            boolean exists = likeService.existsByUserIdAndPostId(user_id, post_id);
            long likeCount = likeService.getCountLike(post_id);

            LikesDto likesDto = new LikesDto();
            likesDto.setExists(exists);
            likesDto.setLikeCount(likeCount);

            return ResponseEntity.ok(likesDto);
        }catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @ResponseBody
    @GetMapping("/add")
    public ResponseEntity<?> addLike(@RequestParam(value = "user_id") long user_id,
                                  @RequestParam(value = "post_id") long post_id) {

        try {
            likeService.addLike(user_id, post_id);
            long likeCount = likeService.getCountLike(post_id);

            return ResponseEntity.ok(likeCount);
        }catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @ResponseBody
    @GetMapping("/cancel")
    public ResponseEntity<?> cancelLike(@RequestParam(value = "user_id") long user_id,
                                     @RequestParam(value = "post_id") long post_id) {

        try {
            likeService.cancelLike(user_id, post_id);
            long likeCount = likeService.getCountLike(post_id);

            return ResponseEntity.ok(likeCount);
        }catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }
}
