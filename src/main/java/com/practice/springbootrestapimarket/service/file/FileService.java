package com.practice.springbootrestapimarket.service.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    void upload(MultipartFile multipartFile, String uniqueName);

    void delete(String filename);
}