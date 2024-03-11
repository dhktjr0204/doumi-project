package com.example.doumiproject.controller;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.exception.user.NotValidateUserException;
import com.example.doumiproject.service.CommentService;
import com.example.doumiproject.validate.CommentValidator;
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
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/ordertime")
    public String orderCommentByCreateAt(@RequestParam("id") long postId, @RequestParam("type") String type, Model model,HttpSession session) {
        long userId = (long)session.getAttribute("userId");

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
    public String orderCommentByLikeCount(@RequestParam("id") long postId, @RequestParam("type") String type, Model model, HttpSession session) {
        long userId = (long)session.getAttribute("userId");

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
    public String addComment(@ModelAttribute("newComment") Comment comment
            ,BindingResult result, Model model,HttpSession session) {

        validateComment(comment, result);

        long userId = (long)session.getAttribute("userId");;

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
    public String getEditForm(@RequestBody Comment comment, Model model,HttpSession session) {

        long userId = (long)session.getAttribute("userId");

        if(userId!=comment.getUserId()){
            throw new NotValidateUserException();
        }

        model.addAttribute("comment", comment);

        return "comment/commentEditForm";
    }

    @PutMapping("/edit")
    public String editComment(@RequestParam("id") long commentId, @ModelAttribute("comment") Comment comment,
                              BindingResult result, Model model,HttpSession session) {

        validateComment(comment, result);

        long userId = (long)session.getAttribute("userId");

        if(userId!=comment.getUserId()){
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
    public String deleteComment(@RequestParam("postId") long postId, @RequestParam("commentId") long commentId, @RequestParam("userId") long author
            , Model model, HttpSession session) {
        long userId = (long)session.getAttribute("userId");;

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

    private void validateComment(Comment comment, BindingResult result){
        CommentValidator commentValidator = new CommentValidator();
        commentValidator.validate(comment, result);
    }

}
