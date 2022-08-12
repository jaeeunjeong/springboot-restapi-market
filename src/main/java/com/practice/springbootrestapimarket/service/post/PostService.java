package com.practice.springbootrestapimarket.service.post;

import com.practice.springbootrestapimarket.dto.post.PostCreateRequest;
import com.practice.springbootrestapimarket.dto.post.PostCreateResponse;
import com.practice.springbootrestapimarket.entity.post.Image;
import com.practice.springbootrestapimarket.entity.post.Post;
import com.practice.springbootrestapimarket.repository.category.CategoryRepository;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import com.practice.springbootrestapimarket.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;

    public PostCreateResponse create(PostCreateRequest req) {
        Post post = postRepository.save(
                PostCreateRequest.toEntity(
                        req,
                        memberRepository,
                        categoryRepository
                )
        );

        uploadImages(post.getImages(), req.getImages());
        return new PostCreateResponse(post.getId());
    }

    private void uploadImages(List<Image> postImages, List<MultipartFile> fileImages) {

    }
}
