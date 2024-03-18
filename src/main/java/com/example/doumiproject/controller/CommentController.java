package com.example.doumiproject.controller;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.exception.user.NotValidateUserException;
import com.example.doumiproject.service.CommentService;
import com.example.doumiproject.validate.CommentValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
@Tag(name = "Comment", description = "Comment Controller")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/ordertime")
    @Operation(summary = "최신순으로 정렬된 댓글을 조회할 수 있는 API", description = "query로 게시글의 id와 게시글의 type을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "최신순으로 정렬된 댓글 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String orderCommentByCreateAt(@RequestParam("id") long postId,
        @RequestParam("type") String type, Model model, HttpSession session) {
        long userId = (long) session.getAttribute("userId");

        List<CommentDto> comments = commentService.getAllComments(postId, userId);

        model.addAttribute("comments", comments);
        model.addAttribute("postId", postId);
        if (type.equals("QUIZ")) {
            model.addAttribute("newComment", new Comment("QUIZ"));
        } else {
            model.addAttribute("newComment", new Comment("POST"));
        }

        return "comment/comment::.comment-main";
    }

    @PostMapping("/orderlike")
    @Operation(summary = "좋아요순으로 정렬된 댓글을 조회할 수 있는 API", description = "query로 게시글의 id와 게시글의 type을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "좋아요순으로 정렬된 댓글 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String orderCommentByLikeCount(@RequestParam("id") long postId,
        @RequestParam("type") String type, Model model, HttpSession session) {
        long userId = (long) session.getAttribute("userId");

        List<CommentDto> comments = commentService.getAllCommentsOrderByLikeCount(postId, userId);

        model.addAttribute("comments", comments);
        model.addAttribute("postId", postId);
        if (type.equals("QUIZ")) {
            model.addAttribute("newComment", new Comment("QUIZ"));
        } else {
            model.addAttribute("newComment", new Comment("POST"));
        }
        return "comment/comment::.comment-main";
    }

    @PostMapping("/add")
    @Operation(summary = "댓글 작성 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "최신순으로 정렬된 댓글 HTML을 반환",
            content = @Content(mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 입력 값",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "댓글 길이 입력 오류", value = "{\"errormsg\": \"댓글 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "댓글 내용 입력 오류", value = "{\"errormsg\": \"댓글 내용이 비어있습니다.\", \"errorcode\":400}")
                }))
    })
    public String addComment(@ModelAttribute("newComment") Comment comment
        , BindingResult result, Model model, HttpSession session) {

        validateComment(comment, result);

        long userId = (long) session.getAttribute("userId");
        ;

        //로그인 기능 생기면 userId 방식 수정해야함
        commentService.saveComment(comment, userId);

        long post_id = comment.getPostId();
        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments = commentService.getAllComments(post_id, userId);

        model.addAttribute("comments", comments);
        model.addAttribute("postId", post_id);

        if (comment.getType().equals("QUIZ")) {
            model.addAttribute("newComment", new Comment("QUIZ"));
        } else {
            model.addAttribute("newComment", new Comment("COTE"));
        }

        return "comment/comment";
    }

    @PostMapping("/editForm")
    @Operation(summary = "댓글 수정 폼을 조회할 수 있는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "댓글을 수정할 수 있는 HTML을 반환",
            content = @Content(mediaType = "text/html")),
        @ApiResponse(responseCode = "401",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(name = "댓글 길이 입력 오류", value = "{\"errormsg\": \"인증되지 않은 사용자입니다.\", \"errorcode\":401}")))
    })
    public String getEditForm(@RequestBody Comment comment, Model model, HttpSession session) {

        long userId = (long) session.getAttribute("userId");

        if (userId != comment.getUserId()) {
            throw new NotValidateUserException();
        }

        model.addAttribute("comment", comment);

        return "comment/commentEditForm";
    }

    @PutMapping("/edit")
    @Operation(summary = "댓글 수정 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정한 댓글을 반영한 댓글 HTML 반환",
            content = @Content(mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 입력 값",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "댓글 길이 입력 오류", value = "{\"errormsg\": \"댓글 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "댓글 내용 입력 오류", value = "{\"errormsg\": \"댓글 내용이 비어있습니다.\", \"errorcode\":400}")
                })),
        @ApiResponse(responseCode = "401",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(name = "댓글 길이 입력 오류", value = "{\"errormsg\": \"인증되지 않은 사용자입니다.\", \"errorcode\":401}")))
    })
    public String editComment(@RequestParam("id") long commentId,
        @ModelAttribute("comment") Comment comment,
        BindingResult result, Model model, HttpSession session) {

        validateComment(comment, result);

        long userId = (long) session.getAttribute("userId");

        if (userId != comment.getUserId()) {
            throw new NotValidateUserException();
        }

        //댓글 업데이트
        commentService.updateComment(comment, commentId);

        long post_id = comment.getPostId();
        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments = commentService.getAllComments(comment.getPostId(), userId);

        model.addAttribute("comments", comments);
        model.addAttribute("postId", post_id);

        System.out.println(comment.getType());

        if (comment.getType().equals("QUIZ")) {
            model.addAttribute("newComment", new Comment("QUIZ"));
        } else {
            model.addAttribute("newComment", new Comment("COTE"));
        }

        return "comment/comment";
    }

    @DeleteMapping("/delete")
    @Operation(summary = "댓글 삭제 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "삭제한 댓글을 반영한 댓글 HTML 반환",
            content = @Content(mediaType = "text/html")),
        @ApiResponse(responseCode = "401",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(name = "댓글 길이 입력 오류", value = "{\"errormsg\": \"인증되지 않은 사용자입니다.\", \"errorcode\":401}")))
    })
    public String deleteComment(@RequestParam("postId") long postId,
        @RequestParam("commentId") long commentId, @RequestParam("userId") long author
        , Model model, HttpSession session) {
        long userId = (long) session.getAttribute("userId");
        ;

        if (author != userId) {
            throw new NotValidateUserException();
        }

        commentService.deleteComment(commentId);

        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments = commentService.getAllComments(postId, userId);

        model.addAttribute("postId", postId);
        model.addAttribute("comments", comments);
        model.addAttribute("newComment", new Comment());

        return "comment/comment";

    }

    private void validateComment(Comment comment, BindingResult result) {
        CommentValidator commentValidator = new CommentValidator();
        commentValidator.validate(comment, result);
    }

}
