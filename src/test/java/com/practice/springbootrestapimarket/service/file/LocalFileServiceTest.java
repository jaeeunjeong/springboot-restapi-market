package com.practice.springbootrestapimarket.service.file;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

class LocalFileServiceTest {

    LocalFileService localFileService = new LocalFileService();
    String testLocation = new File("src/test/resources/static").getAbsolutePath();

    @BeforeEach
    void beforeEach() throws IOException {
        ReflectionTestUtils.setField(localFileService, "location", testLocation);
        FileUtils.cleanDirectory(new File(testLocation));
    }

    @DisplayName("업로드 테스트")
    @Test
    void test1() {
        // given
        MultipartFile file = new MockMultipartFile("sample", "sample.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes());
        String filename = "sample.txt";

        // when
        localFileService.upload(file, filename);

        // then
        assertThat(isExists(testLocation + filename)).isTrue();
    }

    boolean isExists(String route) {
        return new File(route).exists();
    }
}