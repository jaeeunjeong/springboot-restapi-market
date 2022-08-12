package com.practice.springbootrestapimarket.dto.post;

import com.practice.springbootrestapimarket.entity.post.Image;
import com.practice.springbootrestapimarket.entity.post.Post;
import com.practice.springbootrestapimarket.exception.CategoryNotFoundException;
import com.practice.springbootrestapimarket.exception.MemberNotFoundException;
import com.practice.springbootrestapimarket.repository.category.CategoryRepository;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.PositiveOrZero;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@ApiModel(value = "게시글 생성 요청")
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

    @ApiModelProperty(value = "게시글 제목", notes = "제목을 입력해주세요.", required = true)
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "게시글 내용", notes = "내용을 입력해주세요.", hidden = true)
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "가격", notes = "가격을 입력해주세요", required = true, example = "10000")
    @NotNull(message = "가격을 입력해주세요.")
    @PositiveOrZero(message = "0원 이상을 입력해주세요.")
    private Long price;

    @ApiModelProperty(hidden = true)
    @Null
    private Long memberId;

    @ApiModelProperty(value = "카테고리 아이디", notes = "카테고리 아이디를 입력해주세요.", required = true, example = "3")
    @NotNull(message = "카테고리 아이디를 입력해주세요.")
    @PositiveOrZero(message = "올바른 카테고리 아이디를 입력해주세요")
    private Long categoryId;

    @ApiModelProperty(value = "이미지", notes = "이미지를 첨부해주세요.")
    private List<MultipartFile> images = new ArrayList<>();

    public static Post toEntity(PostCreateRequest req, MemberRepository memberRepository, CategoryRepository categoryRepository) {
        return new Post(
                req.title,
                req.content,
                req.price,
                memberRepository.findById(req.getMemberId()).orElseThrow(MemberNotFoundException::new),
                categoryRepository.findById(req.getCategoryId()).orElseThrow(CategoryNotFoundException::new),
                req.images.stream().map(i -> new Image(i.getOriginalFilename())).collect(toList())
        );
    }
}
