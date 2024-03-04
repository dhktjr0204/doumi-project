package com.example.doumiproject.controller;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/ordertime")
    public String orderCommentByCreateAt(@RequestParam("id") long postId, @RequestParam("type") String type, Model model) {
        long userId = 1;

        List<CommentDto> comments = commentService.getAllComments(postId, userId);

        model.addAttribute("comments", comments);
        model.addAttribute("postId", postId);
        if (type.equals("QUIZ")) {
            model.addAttribute("newComment", new Comment("QUIZ"));
        }else{
            model.addAttribute("newComment", new Comment("POST"));
        }

        return "comment/comment::.comment-main";
    }

    @PostMapping("/orderlike")
    public String orderCommentByLikeCount(@RequestParam("id") long postId, @RequestParam("type") String type, Model model) {
        long userId = 1;

        List<CommentDto> comments = commentService.getAllCommentsOrderByLikeCount(postId, userId);

        model.addAttribute("comments", comments);
        model.addAttribute("postId", postId);
        if (type.equals("QUIZ")) {
            model.addAttribute("newComment", new Comment("QUIZ"));
        }else{
            model.addAttribute("newComment", new Comment("POST"));
        }
        return "comment/comment::.comment-main";
    }

    @PostMapping("/add")
    public String addComment(@ModelAttribute("newComment") Comment comment, Model model) {
        long userId = 1;

        //로그인 기능 생기면 userId 방식 수정해야함
        commentService.saveComment(comment, 1);

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
    public String getEditForm(@RequestBody Comment comment, Model model) {

        model.addAttribute("comment", comment);

        return "comment/commentEditForm";
    }

    @PostMapping("/edit")
    public String editComment(@RequestParam("id") long commentId, @ModelAttribute("comment") Comment comment, Model model) {
        long userId = 1;

        //댓글 업데이트
        commentService.updateComment(comment, commentId);

        long post_id = comment.getPostId();
        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments = commentService.getAllComments(comment.getPostId(), userId);

        model.addAttribute("comments", comments);
        model.addAttribute("postId", post_id);

        if (comment.getType() == "QUIZ") {
            model.addAttribute("newComment", new Comment("QUIZ"));
        } else {
            model.addAttribute("newComment", new Comment("COTE"));
        }

        return "comment/comment";
    }

    @DeleteMapping("/delete")
    public String deleteComment(@RequestParam("postId") long postId, @RequestParam("commentId") long commentId, Model model) {
        long userId = 1;

        commentService.deleteComment(commentId);

        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments = commentService.getAllComments(postId, userId);

        model.addAttribute("postId", postId);
        model.addAttribute("comments", comments);
        model.addAttribute("newComment", new Comment());

        return "comment/comment";

    }

}
