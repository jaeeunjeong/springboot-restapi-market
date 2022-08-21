package com.practice.springbootrestapimarket.controller.post;

import com.practice.springbootrestapimarket.dto.post.PostCreateRequest;
import com.practice.springbootrestapimarket.service.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static com.practice.springbootrestapimarket.factory.dto.PostCreateRequestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {

    @InjectMocks
    PostController postController;
    @Mock
    PostService postService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @DisplayName("포스팅 등록 확인")
    @Test
    void test1() throws Exception {
        // given
        ArgumentCaptor<PostCreateRequest> postCreateRequestArgumentCaptor = ArgumentCaptor.forClass(PostCreateRequest.class);
        List<MultipartFile> images = new ArrayList<>();
        images.add(new MockMultipartFile("test1", "test1.PNG", MediaType.IMAGE_PNG_VALUE, "image1".getBytes()));
        images.add(new MockMultipartFile("test2", "test2.PNG", MediaType.IMAGE_PNG_VALUE, "image2".getBytes()));
        PostCreateRequest req = postCreateRequestWithImages(images);

        // when, then
        mockMvc.perform(
                multipart("/api/posts")
                        .file("images", images.get(0).getBytes())
                        .file("images", images.get(1).getBytes())
                        .param("title", req.getTitle())
                        .param("content", req.getContent())
                        .param("price", String.valueOf(req.getPrice()))
                        .param("categoryId", String.valueOf(req.getCategoryId()))
                        .with(requestPostProcessor -> {
                            requestPostProcessor.setMethod("POST");
                            return requestPostProcessor;
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated());

        verify(postService).create(req);
        PostCreateRequest createRequest = postCreateRequestArgumentCaptor.capture();
        assertThat(createRequest.getImages().size()).isEqualTo(2);
    }
}