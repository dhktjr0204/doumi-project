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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "CodingTest", description = "CodingTest Controller")
public class CodingTestController {

    private final CodingTestService codingTestService;
    private final CommentService commentService;

    private int pageSize = 10;

    @GetMapping("/doumiAlgorithm")
    @Operation(summary = "알고리즘 메인 페이지를 조회할 수 있는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "알고리즘 메인 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String index() {

        return "codingtest/doumiAlgorithm";
    }

    @GetMapping("/codingtest/index")
    @Operation(summary = "코딩테스트 질문 메인페이지 조회 API", description =
        "코딩테스트 질문 메인페이지를 조회하는 API며, 페이징을 포함합니다."
            + "query로 page 번호를 줄 수 있으며 기본값은 1입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "코딩테스트 질문 메인페이지 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String board(@RequestParam(defaultValue = "1") int page, Model model) {
        if (page < 1) {
            page = 1;
        }

        setPaginationAttributes(model, page,
            codingTestService.getTotalPages(pageSize),
            codingTestService.getAllCodingTest(page, pageSize));

        return "codingtest/index";
    }

    @GetMapping("/codingtest/search")
    @Operation(summary = "코딩테스트 질문글의 제목과 내용을 검색한 결과를 조회하는 API", description =
        "코딩테스트 질문글의 제목,내용 검색 결과를 조회하는 API며, "
            + "query로 keyword와 page 번호를 줄 수 있으며 page 기본값은 1입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "코딩테스트 질문글의 제목,내용 검색 결과 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String search(@RequestParam(value = "keyword") String keyword,
        @RequestParam(defaultValue = "1", value = "page") int page, Model model) {

        if (page < 1) {
            page = 1;
        }

        setPaginationAttributes(model, page,
            codingTestService.getTotalPagesForSearch(pageSize, keyword),
            codingTestService.getSearchCodingTest(keyword, page, pageSize));
        model.addAttribute("keyword", keyword);

        return "codingtest/search";
    }

    @GetMapping("/codingtest/board")
    @Operation(summary = "코딩테스트 질문 게시글을 조회할 수 있는 API", description = "특정 코딩테스트 질문 게시글을 조회할 수 있는 API로 query로 게시글의 id값을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "특정 코딩테스트 질문 게시글을 조회한 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
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
    @Operation(summary = "코딩테스트 질문을 할 수 있는 페이지 조회 API", description = "유저가 질문을 낼 수 있는 페이지로 이동하는 API입니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "코딩테스트 질문 form HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String createCodingTest(Model model) {

        model.addAttribute("cote", new CodingTestRequestDto());

        return "codingtest/form";
    }

    @PostMapping("/codingtest/post")
    @Operation(summary = "코딩테스트 질문을 하는 API", description = "유저가 질문을 등록하면 내용들이 DB에 저장되고 자신이 질문한 게시글로 이동합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "유저가 질문한 코테 게시글 HTML을 반환",
            content = @Content(mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 입력 값",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "질문 제목 입력 오류", value = "{\"errormsg\": \"제목이 비어있습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "질문 제목 입력 오류", value = "{\"errormsg\": \"제목 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "질문 본문 입력 오류", value = "{\"errormsg\": \"본문이 비어있습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "질문 본문 입력 오류", value = "{\"errormsg\": \"본문 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "정답 입력 오류", value = "{\"errormsg\": \"정답 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}")
                }))

    })
    public ResponseEntity<String> postCodingTest(CodingTestRequestDto cote, BindingResult result,
        HttpSession session) {

        validateCodingTest(cote, result);

        long userId = (long) session.getAttribute("userId");

        Long postId = codingTestService.saveCodingTest(cote, userId);

        return ResponseEntity.ok("/codingtest/board?id=" + postId);
    }

    @GetMapping("/codingtest/edit")
    @Operation(summary = "질문을 수정할 수 있는 페이지 조회 API", description = "유저가 등록한 질문 내용을 수정할 수 있는 페이지를 조회하는 API로 query로 게시글의 id값을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "질문 수정 form HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
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
    @Operation(summary = "질문을 수정할 수 있는 API", description = "유저가 등록한 질문 내용을 수정할 수 있는 API로 query로 게시글의 id값을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정된 질문 게시글 HTML을 반환",
            content = @Content(mediaType = "text/html")),
        @ApiResponse(responseCode = "400", description = "잘못된 요청 입력 값",
            content = @Content(mediaType = "application/json",
                examples = {
                    @ExampleObject(name = "질문 제목 입력 오류", value = "{\"errormsg\": \"제목이 비어있습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "질문 제목 입력 오류", value = "{\"errormsg\": \"제목 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "질문 본문 입력 오류", value = "{\"errormsg\": \"본문이 비어있습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "질문 본문 입력 오류", value = "{\"errormsg\": \"본문 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}"),
                    @ExampleObject(name = "정답 입력 오류", value = "{\"errormsg\": \"정답 길이가 최대 길이를 초과하였습니다.\", \"errorcode\":400}")
                })),
        @ApiResponse(responseCode = "401",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(name = "사용자 인증 오류", value = "{\"errormsg\": \"인증되지 않은 사용자입니다.\", \"errorcode\":401}")))
    })
    public ResponseEntity<String> updateCodingTest(@RequestParam("id") Long id,
        CodingTestRequestDto cote,
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
    @Operation(summary = "퀴즈를 삭제할 수 있는 API", description = "유저가 등록한 질문을 삭제하는 API로 query로 게시글의 id값을 줄 수 있습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "코딩테스트 메인페이지 HTML을 반환 ",
            content = @Content(mediaType = "text/html")),
        @ApiResponse(responseCode = "401",
            content = @Content(mediaType = "application/json",
                examples = @ExampleObject(name = "사용자 인증 오류", value = "{\"errormsg\": \"인증되지 않은 사용자입니다.\", \"errorcode\":401}")))
    })
    public ResponseEntity<String> deleteCodingTest(@RequestParam("postId") long postId,
        @RequestParam("userId") long author, HttpSession session) {

        long userId = (long) session.getAttribute("userId");

        if (author != userId) {
            throw new NotValidateUserException();
        }

        codingTestService.deleteCodingTest(postId);

        return ResponseEntity.ok("/quiz");
    }

    private void setPaginationAttributes(Model model, int page, int totalPages,
        List<PostDto> cotes) {

        int startIdx = PaginationUtil.calculateStartIndex(page);
        int endIdx = PaginationUtil.calculateEndIndex(page, totalPages);

        model.addAttribute("cotes", cotes);
        model.addAttribute("currentPage", page);
        model.addAttribute("startIdx", startIdx);
        model.addAttribute("endIdx", endIdx);
        model.addAttribute("totalPages", totalPages);
    }

    private void validateCodingTest(CodingTestRequestDto cote, BindingResult result) {
        CodingTestValidator codingTestValidator = new CodingTestValidator();
        codingTestValidator.validate(cote, result);
    }

}
