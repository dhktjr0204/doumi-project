package com.example.doumiproject.controller;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.CodingTestDto;
import com.example.doumiproject.dto.CodingTestRequestDto;
import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.entity.Comment;
import com.example.doumiproject.exception.user.NotValidateUserException;
import com.example.doumiproject.service.CommentService;
import com.example.doumiproject.service.CodingTestService;
import com.example.doumiproject.util.PaginationUtil;
import com.example.doumiproject.validate.CodingTestValidator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CodingTestController {

    private final CodingTestService codingTestService;
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
                codingTestService.getTotalPages(pageSize), codingTestService.getAllCodingTest(page, pageSize));

        return "codingtest/index";
    }

    @GetMapping("/codingtest/search")
    public String search(@RequestParam(value = "keyword") String keyword,
                         @RequestParam(defaultValue = "1", value = "page") int page, Model model) {

        if (page < 1) {
            page = 1;
        }

        setPaginationAttributes(model, page,
                codingTestService.getTotalPagesForSearch(pageSize, keyword), codingTestService.getSearchCodingTest(keyword, page, pageSize));
        model.addAttribute("keyword", keyword);

        return "codingtest/search";
    }

    @GetMapping("/codingtest/board")
    public String getCoteDetail(@RequestParam("id") Long id, Model model, HttpSession session) {

        long userId = (long) session.getAttribute("userId");

        //글의 상세 정보 가져오기
        CodingTestDto cote = codingTestService.getCodingTest(id, userId);
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
    public String createCodingTest(Model model) {

        model.addAttribute("cote", new CodingTestRequestDto());

        return "codingtest/form";
    }

    @PostMapping("/codingtest/post")
    public ResponseEntity<String> postCodingTest(CodingTestRequestDto cote, BindingResult result, HttpSession session) {

        validateCodingTest(cote, result);

        long userId = (long) session.getAttribute("userId");

        Long postId = codingTestService.saveCodingTest(cote, userId);

        return ResponseEntity.ok("/codingtest/board?id=" + postId);
    }

    @GetMapping("/codingtest/edit")
    public String editCodingTest(@RequestParam("id") Long id, Model model, HttpSession session) {
        long userId = (long) session.getAttribute("userId");

        CodingTestDto cote = codingTestService.getCodingTest(id, userId);

        if (userId != cote.getUserId()) {
            return "error/404";
        }

        model.addAttribute("cote", cote);

        return "codingtest/edit";
    }

    @PutMapping("/codingtest/edit")
    public ResponseEntity<String> updateCodingTest(@RequestParam("id") Long id, CodingTestRequestDto cote,
                                                   BindingResult result, HttpSession session) {

        validateCodingTest(cote, result);

        long userId = (long) session.getAttribute("userId");

        //수정 권한있는 사용자인지 검증 로직 repository에 수정필요
        codingTestService.updateCodingTest(cote, id);

        if (userId != cote.getUserId()) {
            throw new NotValidateUserException();
        }
        return ResponseEntity.ok("/codingtest/board?id=" + id);
    }

    @DeleteMapping("/codingtest/delete")
    public ResponseEntity<String> deleteCodingTest(@RequestParam("postId") long postId, @RequestParam("userId") long author, HttpSession session) {

        long userId = (long) session.getAttribute("userId");

        if (author != userId) {
            throw new NotValidateUserException();
        }

        codingTestService.deleteCodingTest(postId);

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

    private void validateCodingTest(CodingTestRequestDto cote, BindingResult result){
        CodingTestValidator codingTestValidator = new CodingTestValidator();
        codingTestValidator.validate(cote, result);
    }

}
