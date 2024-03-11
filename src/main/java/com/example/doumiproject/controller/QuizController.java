package com.example.doumiproject.controller;

import com.example.doumiproject.dto.*;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.exception.user.NotValidateUserException;
import com.example.doumiproject.service.CommentService;
import com.example.doumiproject.service.QuizService;
import com.example.doumiproject.util.PaginationUtil;
import jakarta.servlet.http.HttpServletRequest;
import com.example.doumiproject.validate.QuizValidator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/quiz")
public class QuizController {

    private final QuizService quizService;
    private final CommentService commentService;

    private int pageSize = 10;

    @GetMapping("")
    public String index(@RequestParam(defaultValue = "1") int page, Model model) {

        if (page < 1) {
            page = 1;
        }

        setPaginationAttributes(model, page,
                quizService.getTotalPages(pageSize), quizService.getAllQuiz(page, pageSize));

        return "quiz/index";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "keyword") String keyword,
                         @RequestParam(defaultValue = "1", value = "page") int page, Model model) {

        if (page < 1) {
            page = 1;
        }

        setPaginationAttributes(model, page,
                quizService.getTotalPagesForSearch(pageSize, keyword), quizService.getSearchQuiz(keyword, page, pageSize));
        model.addAttribute("keyword", keyword);

        return "quiz/search";
    }

    @GetMapping("/tag")
    public String searchTags(@RequestParam(value = "name") String tag,
                             @RequestParam(defaultValue = "1", value = "page") int page, Model model) {

        if (page < 1) {
            page = 1;
        }

        setPaginationAttributes(model, page,
                quizService.getTotalPagesForSelectedTag(pageSize, tag),
                quizService.getQuizForSelectedTag(tag, page, pageSize));
        model.addAttribute("tag", tag);

        return "quiz/tag";
    }

    private void setPaginationAttributes(Model model, int page, int totalPages, List<PostDto> quizs) {

        int startIdx = PaginationUtil.calculateStartIndex(page);
        int endIdx = PaginationUtil.calculateEndIndex(page, totalPages);

        model.addAttribute("quizs", quizs);
        model.addAttribute("currentPage", page);
        model.addAttribute("startIdx", startIdx);
        model.addAttribute("endIdx", endIdx);
        model.addAttribute("totalPages", totalPages);
    }

    @GetMapping("/board")
    public String getQuizDetail(@RequestParam("id") Long id, Model model, HttpSession session) {

        long userId = (long) session.getAttribute("userId");

        //글의 상세 정보 가져오기
        QuizDto quiz = quizService.getQuiz(id, userId);
        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments = commentService.getAllComments(id, userId);

        model.addAttribute("quiz", quiz);
        model.addAttribute("postId", quiz.getId());
        model.addAttribute("comments", comments);
        model.addAttribute("newComment", new Comment("QUIZ"));

        return "quiz/board";
    }

    @GetMapping("/post")
    public String getPostQuizForm(Model model) {

        //타입별 태그 모두 불러오기
        List<TagDto> tags = quizService.getAllTags();

        model.addAttribute("tags", tags);
        model.addAttribute("quiz", new QuizRequestDto());

        return "quiz/form";
    }

    @PostMapping("/post")
    public ResponseEntity<String> postQuiz(QuizRequestDto quiz, BindingResult result, HttpSession session) {

        validateQuiz(quiz, result);

        long userId = (long) session.getAttribute("userId");

        Long postId = quizService.saveQuiz(quiz, userId);

        return ResponseEntity.ok("/quiz/board?id=" + postId);
    }

    @GetMapping("/edit")
    public String getEditQuizForm(@RequestParam("id") Long id, Model model, HttpSession session) {
        long userId=(long) session.getAttribute("userId");

        QuizDto quiz = quizService.getQuiz(id, userId);
        List<TagDto> tags = quizService.getAllTags();

        //현재 로그인 된 유저가 글쓴 유저가 아닐 경우 예외처리
        if(userId!=quiz.getUserId()){
            return "error/404";
        }

        model.addAttribute("quiz", quiz);
        model.addAttribute("postId", quiz.getId());
        model.addAttribute("tags", tags);

        return "quiz/edit";
    }

    @PutMapping("/edit")
    public ResponseEntity<String> updateQuiz(@RequestParam("id") Long id, QuizRequestDto quiz,
                                             BindingResult result, HttpSession session) {

        validateQuiz(quiz, result);

        long userId = (long) session.getAttribute("userId");

        //현재 로그인 된 유저가 글쓴 유저가 아닐 경우 예외처리
        if (quiz.getUserId() != userId) {
            throw new NotValidateUserException();
        }

        quizService.updateQuiz(quiz, id);

        return ResponseEntity.ok("/quiz/board?id=" + id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteQuiz(@RequestParam("postId") long postId, @RequestParam("userId") long author, HttpSession session) {

        long userId = (long) session.getAttribute("userId");

        if (author != userId) {
            throw new NotValidateUserException();
        }

        quizService.deleteQuiz(postId);

        return ResponseEntity.ok("/quiz");
    }

    private void validateQuiz(QuizRequestDto quiz, BindingResult result){
        QuizValidator quizValidator = new QuizValidator();
        quizValidator.validate(quiz, result);
    }
}
