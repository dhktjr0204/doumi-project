package com.example.doumiproject.controller;

import com.example.doumiproject.dto.*;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.dto.QuizRequestDto;
import com.example.doumiproject.service.CommentService;
import com.example.doumiproject.service.QuizService;
import com.example.doumiproject.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
                quizService.getTotalPages(pageSize, keyword), quizService.getSearchQuiz(keyword, page, pageSize));
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
    public String getQuizDetail(@RequestParam("id") Long id, Model model){

        long userId = 1;
        //글의 상세 정보 가져오기
        QuizDto quiz = quizService.getQuiz(id, userId);
        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments = commentService.getAllComments(id, userId);

        model.addAttribute("quiz",quiz);
        model.addAttribute("postId", quiz.getId());
        model.addAttribute("comments",comments);
        model.addAttribute("newComment",new Comment("QUIZ"));

        return "quiz/board";
    }

    @GetMapping("/post")
    public String getPostQuizForm(Model model){
        //로그인 하지 않은 유저가 요청하면 거절

        //타입별 태그 모두 불러오기
        List<TagDto> tags = quizService.getAllTags();

        model.addAttribute("tags",tags);
        model.addAttribute("quiz",new QuizRequestDto());

        return "quiz/form";
    }

    @PostMapping("/post")
    public ResponseEntity<String> postQuiz(QuizRequestDto quiz) {

        //로그인 하지 않은 유저가 요청하면 거절
        Long postId = quizService.saveQuiz(quiz, 1l);

        return ResponseEntity.ok("/quiz/board?id="+postId);
    }

    @GetMapping("/edit")
    public String getEditQuizForm(@RequestParam("id") Long id, Model model){
        long userId=1l;

        //로그인 생기면 현재 로그인된 유저의 nickname과 quizDetail의 userId가 일치한지 검증 필요
        QuizDto quiz=quizService.getQuiz(id, userId);
        List<TagDto> tags = quizService.getAllTags();

        if(quiz.getUserId()!=userId){
            System.out.println("에러 처리");
        }

        model.addAttribute("quiz",quiz);
        model.addAttribute("postId", quiz.getId());
        model.addAttribute("tags",tags);

        return "quiz/edit";
    }

    @PostMapping("/edit")
    public ResponseEntity<String> updateQuiz(@RequestParam("id") Long id, QuizRequestDto quiz){

        long userId=1l;

        if(quiz.getUserId()!=userId){
            System.out.println("에러처리");
        }

        quizService.updateQuiz(quiz, id);

        return ResponseEntity.ok("/quiz/board?id="+id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteQuiz(@RequestParam("id") long id){

        quizService.deleteQuiz(id);

        return ResponseEntity.ok("/quiz");
    }
}
