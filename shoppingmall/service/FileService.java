package com.example.shoppingmall.service;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileService {
    String uploadFile(MultipartFile file) throws IOException;
    void deleteFile(String filePath);
}