package com.example.doumiproject.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AlgorithmController {
    @GetMapping("/doumiAlgorithm")
    public String index() {

        return "algorithm/doumiAlgorithm";
    }

    @GetMapping("/codingtest/timecomplexity")
    public String timecomplexity() {

        return "algorithm/timeComplexity";
    }

    @GetMapping("/codingtest/stack")
    public String stack(){
        return "algorithm/stackAndQueue";
    }
}
