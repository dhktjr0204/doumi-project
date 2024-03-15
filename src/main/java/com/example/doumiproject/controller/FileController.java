package com.example.doumiproject.controller;

import com.example.doumiproject.dto.FileDto;
import com.example.doumiproject.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequiredArgsConstructor
public class FileController{

    private final FileService fileService;

    @PostMapping("/image-upload")
    public String fileWrite(@RequestBody MultipartFile file) throws IOException {

        return fileService.fileWrite(file);
    }

    @GetMapping("/image-print")
    public byte[] printEditorImage(String fileName) throws FileNotFoundException {

        return fileService.fileLoad(fileName);
    }
}
