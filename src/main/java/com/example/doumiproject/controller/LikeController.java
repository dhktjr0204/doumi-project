package com.example.doumiproject.controller;

import com.example.doumiproject.dto.LikesDto;
import com.example.doumiproject.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public ResponseEntity<?> getLikeInfo(HttpServletRequest request, @RequestParam(value = "postId") long postId) {

        try {

            HttpSession session = request.getSession();
            long userId = (long) session.getAttribute("userId");

            boolean exists = likeService.existsByUserIdAndPostId(userId, postId);
            long likeCount = likeService.getCountLike(postId);

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
    public ResponseEntity<?> addLike(HttpServletRequest request,
                                     @RequestParam(value = "postId") long postId,
                                     @RequestParam(value = "type") String type) {

        try {
            HttpSession session = request.getSession();
            long userId = (long) session.getAttribute("userId");

            likeService.addLike(userId, postId, type);
            long likeCount = likeService.getCountLike(postId);

            return ResponseEntity.ok(likeCount);
        }catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @ResponseBody
    @GetMapping("/cancel")
    public ResponseEntity<?> cancelLike(HttpServletRequest request,
                                        @RequestParam(value = "postId") long postId,
                                        @RequestParam(value = "type") String type) {

        try {
            HttpSession session = request.getSession();
            long userId = (long) session.getAttribute("userId");

            likeService.cancelLike(userId, postId, type);
            long likeCount = likeService.getCountLike(postId);

            return ResponseEntity.ok(likeCount);
        }catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }
}
