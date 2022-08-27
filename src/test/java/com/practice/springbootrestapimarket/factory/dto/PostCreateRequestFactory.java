package com.practice.springbootrestapimarket.factory.dto;

import com.practice.springbootrestapimarket.dto.post.PostCreateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

public class PostCreateRequestFactory {
    public static PostCreateRequest postCreateRequest() {
        return new PostCreateRequest("갤럭시 s22 판매", "갤럭시 s22울트라도 팔아요", 80_000_000L, 1L, 1L, Arrays.asList());
    }

    public static PostCreateRequest postCreateRequestWithTitle(String title) {
        return new PostCreateRequest(title, "갤럭시 s22울트라도 팔아요", 80_000_000L, 1L, 1L, Arrays.asList());
    }

    public static PostCreateRequest postCreateRequestWithContent(String Content) {
        return new PostCreateRequest("갤럭시 s22 판매", Content, 80_000_000L, 1L, 1L, Arrays.asList());
    }

    public static PostCreateRequest postCreateRequestWithPrice(Long price) {
        return new PostCreateRequest("갤럭시 s22 판매", "갤럭시 s22울트라도 팔아요", price, 1L, 1L, Arrays.asList());
    }

    public static PostCreateRequest postCreateRequestWithMemberId(Long memberId) {
        return new PostCreateRequest("갤럭시 s22 판매", "갤럭시 s22울트라도 팔아요", 80_000_000L, memberId, 1L, Arrays.asList());
    }

    public static PostCreateRequest postCreateRequestWithCategory(Long category) {
        return new PostCreateRequest("갤럭시 s22 판매", "갤럭시 s22울트라도 팔아요", 80_000_000L, 1L, category, Arrays.asList());
    }

    public static PostCreateRequest postCreateRequestWithImages(List<MultipartFile> images) {
        return new PostCreateRequest("갤럭시 s22 판매", "갤럭시 s22울트라도 팔아요", 80_000_000L, 1L, 1L, images);
    }

    public static PostCreateRequest postCreateRequest(String title, String content, Long price, Long memberId, Long category, List<MultipartFile> images) {
        return new PostCreateRequest(title, content, price, memberId, category, images);
    }
}
