package com.example.doumiproject.controller;

import com.example.doumiproject.dto.CommentDto;
import com.example.doumiproject.dto.CoteDto;
import com.example.doumiproject.dto.PostDto;
import com.example.doumiproject.dto.CoteRequestDto;
import com.example.doumiproject.service.CoteService;
import com.example.doumiproject.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CodingTestController {

    private final CoteService coteService;

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
    public String getCoteDetail(@RequestParam("id") Long id, Model model){

        //글의 상세 정보 가져오기
        CoteDto cote = coteService.getCote(id);
        //글에 연결된 댓글들 가져오기
        List<CommentDto> comments = coteService.getComments(id);

        model.addAttribute("cote",cote);
        model.addAttribute("comments",comments);

        return "codingtest/board";
    }

    @GetMapping("/codingtest/timecomplexity")
    public String timecomplexity() {

        return "codingtest/timeComplexity";
    }

    @GetMapping("/codingtest/post")
    public String createCote(Model model){

        model.addAttribute("cote",new CoteRequestDto());

        return "codingtest/form";
    }
    @PostMapping("/codingtest/post")
    public ResponseEntity<String> postCote(CoteRequestDto cote) {

        Long postId = coteService.saveCote(cote, 1l);

        return ResponseEntity.ok("/codingtest/board?id="+postId);
    }

    @GetMapping("/edit")
    public String editCote(@RequestParam("id") Long id, Model model){


        CoteDto cote=coteService.getCote(id);

        model.addAttribute("cote", cote);

        return "codingtest/edit";
    }

    @PostMapping("/edit")
    public ResponseEntity<String> updateCote(@RequestParam("id") Long id, CoteRequestDto cote){

        //수정 권한있는 사용자인지 검증 로직 repository에 수정필요
        coteService.updateCote(cote, id, 1l);

        return ResponseEntity.ok("/codingtest/board?id="+id);
    }

    @DeleteMapping("/delete")
    public String deleteCote(@RequestParam("id") long id){

        coteService.deleteCote(id);

        return "redirect:/doumiAlgorithm";
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
