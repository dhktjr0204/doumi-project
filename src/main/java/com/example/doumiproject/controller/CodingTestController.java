package com.example.doumiproject.controller;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.CoteDto;
import com.example.doumiproject.dto.CoteRequestDto;
import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.dto.QuizDto;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.exception.user.NotValidateUserException;
import com.example.doumiproject.service.CommentService;
import com.example.doumiproject.service.CoteService;
import com.example.doumiproject.util.PaginationUtil;
import com.example.doumiproject.validate.CoteValidator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CodingTestController {

    private final CoteService coteService;
    private final CommentService commentService;

    private int pageSize = 10;

    @GetMapping("/doumiAlgorithm")
    public String index() {

        return "codingtest/doumiAlgorithm";
    }

    @GetMapping("/codingtest/index")
    public String board(@RequestParam(defaultValue = "1") int page, Model model) {
        if (page < 1) {
            page = 1;
        }

        setPaginationAttributes(model, page,
                coteService.getTotalPages(pageSize), coteService.getAllCote(page, pageSize));

        return "codingtest/index";
    }

    @GetMapping("/codingtest/board")
    public String getCoteDetail(@RequestParam("id") Long id, Model model, HttpSession session) {

        long userId = (long) session.getAttribute("userId");

        //글의 상세 정보 가져오기
        CoteDto cote = coteService.getCote(id, userId);
        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments = commentService.getAllComments(id, userId);

        model.addAttribute("cote", cote);
        model.addAttribute("postId", cote.getId());
        model.addAttribute("comments", comments);
        model.addAttribute("newComment", new Comment("COTE"));

        return "codingtest/board";
    }

    @GetMapping("/codingtest/timecomplexity")
    public String timecomplexity() {

        return "codingtest/timeComplexity";
    }

    @GetMapping("/codingtest/post")
    public String createCote(Model model) {

        model.addAttribute("cote", new CoteRequestDto());

        return "codingtest/form";
    }

    @PostMapping("/codingtest/post")
    public ResponseEntity<String> postCote(CoteRequestDto cote, BindingResult result, HttpSession session) {

        CoteValidator coteValidator = new CoteValidator();
        coteValidator.validate(cote, result);

        long userId=(long) session.getAttribute("userId");

        Long postId = coteService.saveCote(cote, userId);

        return ResponseEntity.ok("/codingtest/board?id=" + postId);
    }

    @GetMapping("/codingtest/edit")
    public String editCote(@RequestParam("id") Long id, Model model, HttpSession session) {
        long userId = (long) session.getAttribute("userId");

        CoteDto cote = coteService.getCote(id, userId);

        if(userId!=cote.getUserId()){
            return "error/404";
        }

        model.addAttribute("cote", cote);

        return "codingtest/edit";
    }

    @PostMapping("/codingtest/edit")
    public ResponseEntity<String> updateCote(@RequestParam("id") Long id, CoteRequestDto cote,
            BindingResult result, HttpSession session) {

        CoteValidator coteValidator = new CoteValidator();
        coteValidator.validate(cote, result);

        long userId = (long) session.getAttribute("userId");

        //수정 권한있는 사용자인지 검증 로직 repository에 수정필요
        coteService.updateCote(cote, id);

        if(userId!=cote.getUserId()){
            throw new NotValidateUserException();
        }
        return ResponseEntity.ok("/codingtest/board?id=" + id);
    }

    @DeleteMapping("/codingtest/delete")
    public ResponseEntity<String> deleteCote(@RequestParam("id") long id) {

        coteService.deleteCote(id);

        return ResponseEntity.ok("/quiz");
    }

    private void setPaginationAttributes(Model model, int page, int totalPages, List<PostDto> cotes) {

        int startIdx = PaginationUtil.calculateStartIndex(page);
        int endIdx = PaginationUtil.calculateEndIndex(page, totalPages);

        model.addAttribute("cotes", cotes);
        model.addAttribute("currentPage", page);
        model.addAttribute("startIdx", startIdx);
        model.addAttribute("endIdx", endIdx);
        model.addAttribute("totalPages", totalPages);
    }

}
