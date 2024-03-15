package com.example.doumiproject.service;


import com.example.doumiproject.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {

    String fileWrite(MultipartFile file) throws IOException;
    byte[] fileLoad(String fileName) throws FileNotFoundException;
}
