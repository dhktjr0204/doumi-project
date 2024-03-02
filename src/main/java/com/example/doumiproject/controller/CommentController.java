package com.example.doumiproject.controller;

import com.example.doumiproject.dto.*;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.service.CommentService;
import com.example.doumiproject.service.QuizService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    private final QuizService quizService;

    @PostMapping("/add")
    public String addComment(@ModelAttribute("newComment") Comment comment, Model model){

        long post_id = comment.getPostId();

        //로그인 기능 생기면 userId 방식 수정해야함
        commentService.saveComment(comment, 1, "QUIZ");

        //글의 상세 정보 가져오기
        //QuizDto quiz=quizService.getQuiz(comment.getPostId());
        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments = commentService.getAllComments(post_id);

        //저장한 댓글 가져오기
        model.addAttribute("postId", post_id);
        model.addAttribute("comments",comments);
        model.addAttribute("newComment",new Comment());

        return "comment/comment";
    }

    @PostMapping("/editForm")
    public String getEditForm(@RequestBody Comment comment, Model model){

        model.addAttribute("comment",comment);

        return "comment/commentEditForm";
    }

    @PostMapping("/edit")
    public String editComment(@RequestParam("id") long commentId, @ModelAttribute("comment") Comment comment,Model model){

        //댓글 업데이트
        commentService.updateComment(comment, commentId);

        //글의 상세 정보 가져오기
        //QuizDto quiz=quizService.getQuiz(comment.getPostId());
        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments=commentService.getAllComments(comment.getPostId());

        //model.addAttribute("quiz",quiz);
        model.addAttribute("comments",comments);
        model.addAttribute("newComment",new Comment());

        return "comment/comment";
    }

    @DeleteMapping("/delete")
    public String deleteComment(@RequestParam("postId") long postId, @RequestParam("commentId") long commentId,Model model){
        commentService.deleteComment(commentId);

        //글의 상세 정보 가져오기
        QuizDto quiz=quizService.getQuiz(postId);
        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments=commentService.getAllComments(postId);

        model.addAttribute("quiz",quiz);
        model.addAttribute("comments",comments);
        model.addAttribute("newComment",new Comment());

        return "comment/comment";

    }

}
