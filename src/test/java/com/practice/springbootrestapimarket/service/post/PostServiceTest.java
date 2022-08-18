package com.practice.springbootrestapimarket.service.post;

import com.practice.springbootrestapimarket.dto.post.PostCreateRequest;
import com.practice.springbootrestapimarket.entity.post.Image;
import com.practice.springbootrestapimarket.exception.CategoryNotFoundException;
import com.practice.springbootrestapimarket.exception.MemberNotFoundException;
import com.practice.springbootrestapimarket.exception.UnsupportedImageFormatException;
import com.practice.springbootrestapimarket.repository.category.CategoryRepository;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import com.practice.springbootrestapimarket.repository.post.PostRepository;
import com.practice.springbootrestapimarket.service.file.FileService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.practice.springbootrestapimarket.factory.dto.PostCreateRequestFactory.postCreateRequest;
import static com.practice.springbootrestapimarket.factory.dto.PostCreateRequestFactory.postCreateRequestWithImages;
import static com.practice.springbootrestapimarket.factory.entity.CategoryFactory.createCategory;
import static com.practice.springbootrestapimarket.factory.entity.ImageFactory.createImage;
import static com.practice.springbootrestapimarket.factory.entity.MemberFactory.createMember;
import static com.practice.springbootrestapimarket.factory.entity.PostFactory.createPostWithImage;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @InjectMocks
    PostService postService;
    @Mock
    PostRepository postRepository;
    @Mock
    MemberRepository memberRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    FileService fileService;

    @DisplayName("정상적으로 포스트가 작성되는 경우")
    @Test
    void test1() {
        // given
        PostCreateRequest req = new PostCreateRequest();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));
        List<Image> list = new ArrayList<Image>();
        list.add(createImage());
        list.add(createImage());
        given(postRepository.save(any())).willReturn(createPostWithImage(list));

        // when
        postService.create(req);

        // then
        verify(postRepository).save(any());
        verify(fileService, times(req.getImages().size())).upload(any(), anyString());
    }

    @DisplayName("회원이 적절하지 않은 경우")
    @Test
    void test2() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> postService.create(postCreateRequest())).isInstanceOf(MemberNotFoundException.class);
    }

    @DisplayName("카테고리가 적절하지 않은 경우")
    @Test
    void test3() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));

        // when, then
        assertThatThrownBy(() -> postService.create(postCreateRequest())).isInstanceOf(CategoryNotFoundException.class);
    }

    @DisplayName("첨부파일이 적절하지 않은 경우")
    @Test
    void test4() {
        // given
        PostCreateRequest req = postCreateRequestWithImages(
                Arrays.asList(new MockMultipartFile("test", "test.txt", MediaType.TEXT_PLAIN_VALUE, "test".getBytes()))
        );
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(createCategory()));

        // when, then
        assertThatThrownBy(() -> postService.create(req)).isInstanceOf(UnsupportedImageFormatException.class);
    }

}