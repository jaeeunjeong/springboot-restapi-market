package learning;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class FileUnitTest {
    String fileLocation = new File("src/test/resources/static").getAbsolutePath() + "/";

    @DisplayName("파일이 바르게 저장되는 지 확인")
    @Test
    void test1() throws Exception {
        // given
        String filePath = fileLocation + ("sample.txt");
        MultipartFile file = new MockMultipartFile("sample", "sample.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes(StandardCharsets.UTF_8));

        // when
        file.transferTo(new File(filePath));

        // then
        boolean before = isExist(filePath);
        assertThat(before).isTrue();
    }

    @DisplayName("파일이 바르게 삭제되는 지 확인")
    @Test
    void test2() throws Exception {
        // given
        String filePath = fileLocation + ("sample2.txt");
        MultipartFile file = new MockMultipartFile("sample2", "sample2.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes(StandardCharsets.UTF_8));
        file.transferTo(new File(filePath));
        boolean before = isExist(filePath);

        // when
        FileUtils.cleanDirectory(new File(fileLocation));

        // then
        boolean after = isExist(filePath);
        assertThat(before).isTrue();
        assertThat(after).isFalse();
    }

    boolean isExist(String filePath) {
        return new File(filePath).exists();
    }
}
