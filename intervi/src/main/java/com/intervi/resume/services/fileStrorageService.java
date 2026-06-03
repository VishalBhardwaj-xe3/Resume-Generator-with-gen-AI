package com.intervi.resume.services;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@Service
public class fileStrorageService {
    
    @Value("${file.upload.dir}")
    private String uploadDir;

    public String saveFile(MultipartFile file) {
        try {
            
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            return fileName;


        } catch (Exception e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }
    }

}
