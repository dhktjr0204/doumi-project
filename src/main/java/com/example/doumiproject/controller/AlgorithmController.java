package com.example.doumiproject.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AlgorithmController {

    @GetMapping("/doumiAlgorithm")
    @Operation(summary = "알고리즘 메인 페이지를 조회할 수 있는 API")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "알고리즘 메인 HTML을 반환",
            content = @Content(mediaType = "text/html"))
    })
    public String index() {

        return "algorithm/doumiAlgorithm";
    }

    @GetMapping("/codingtest/timecomplexity")
    public String timecomplexity() {

        return "algorithm/timeComplexity";
    }

    @GetMapping("/codingtest/stack")
    public String stack() {
        return "algorithm/stackAndQueue";
    }

//    @GetMapping("/codingtest/bruteforce")
//    public String bruteforce() {
//        return "algorithm/bruteforce";
//    }

    @GetMapping("/codingtest/sets&maps")
    public String setsAndMaps() {
        return "algorithm/setsAndMaps";
    }
}
