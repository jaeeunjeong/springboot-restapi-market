package com.practice.springbootrestapimarket.service.file;

import com.practice.springbootrestapimarket.exception.FileUploadFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
@Slf4j
public class LocalFileService implements FileService {

    @Value("{$upload.image.location}")
    private String location;

    @PostConstruct
    void postConstruct() {
        File dir = new File(location);
        if (!dir.exists()) dir.mkdir();
    }

    @Override
    public void upload(MultipartFile multipartFile, String uniqueName) {
        try {
            multipartFile.transferTo(new File(location + uniqueName));
        } catch (IOException e) {
            throw new FileUploadFailureException(e);
        }
    }

    @Override
    public void delete(String filename) {

    }
}