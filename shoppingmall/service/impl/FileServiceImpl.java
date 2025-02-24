package com.example.shoppingmall.service.impl;

import com.example.shoppingmall.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public String uploadFile(MultipartFile file) throws IOException {
        // 确保上传目录存在
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        // 生成唯一的文件名
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = UUID.randomUUID().toString() + extension;

        // 保存文件
        Path filePath = uploadDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath);

        // 返回文件访问路径
        return "/uploads/" + filename;
    }

    @Override
    public void deleteFile(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
            try {
                String filename = filePath.substring(filePath.lastIndexOf("/") + 1);
                Path file = Paths.get(uploadPath, filename);
                Files.deleteIfExists(file);
            } catch (IOException e) {
                // 记录错误但不抛出异常
                e.printStackTrace();
            }
        }
    }
}