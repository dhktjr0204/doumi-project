package com.example.doumiproject.controller;

import com.example.doumiproject.dto.LikesDto;
import com.example.doumiproject.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Like", description = "Like Controller")
public class LikeController {

    private final LikeService likeService;

    @ResponseBody
    @GetMapping("")
    @Operation(summary = "게시글의 총 좋아요 수를 반환하는 API", description = "게시글의 총 좋아요 수를 반환하는 API로 query로 게시글의 id값을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "application/json"
            )),
        @ApiResponse(responseCode = "500",

            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(name = "서버 오류", value = "{\"error\": \"내부 서버 오류가 발생했습니다.\"}")

            ))})
    public ResponseEntity<?> getLikeInfo(HttpServletRequest request,
        @RequestParam(value = "postId") long postId, @RequestParam(value = "type") String type) {

        try {

            HttpSession session = request.getSession();
            long userId = (long) session.getAttribute("userId");

            boolean exists = likeService.existsByUserIdAndPostId(userId, postId);
            long likeCount = likeService.getCountLike(postId, type);

            LikesDto likesDto = new LikesDto();
            likesDto.setExists(exists);
            likesDto.setLikeCount(likeCount);

            return ResponseEntity.ok(likesDto);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @ResponseBody
    @GetMapping("/add")
    @Operation(summary = "게시글에 좋아요를 추가하는 API", description = "게시글에 좋아요를 추가하는 API로 query로 게시글의 id값과 게시글의 type을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "application/json"
            )),
        @ApiResponse(responseCode = "500",

            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(name = "서버 오류", value = "{\"error\": \"내부 서버 오류가 발생했습니다.\"}")

            ))})
    public ResponseEntity<?> addLike(HttpServletRequest request,
        @RequestParam(value = "postId") long postId,
        @RequestParam(value = "type") String type) {

        try {
            HttpSession session = request.getSession();
            long userId = (long) session.getAttribute("userId");

            likeService.addLike(userId, postId, type);
            long likeCount = likeService.getCountLike(postId, type);

            return ResponseEntity.ok(likeCount);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @ResponseBody
    @GetMapping("/cancel")
    @Operation(summary = "게시글에 좋아요를 취소하는 API", description = "게시글에 좋아요를 취소하는 API로 query로 게시글의 id값과 게시글의 type을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200",
            content = @Content(mediaType = "application/json"
            )),
        @ApiResponse(responseCode = "500",

            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(name = "서버 오류", value = "{\"error\": \"내부 서버 오류가 발생했습니다.\"}")

            ))})
    public ResponseEntity<?> cancelLike(HttpServletRequest request,
        @RequestParam(value = "postId") long postId,
        @RequestParam(value = "type") String type) {

        try {
            HttpSession session = request.getSession();
            long userId = (long) session.getAttribute("userId");

            likeService.cancelLike(userId, postId, type);
            long likeCount = likeService.getCountLike(postId, type);

            return ResponseEntity.ok(likeCount);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }
}
