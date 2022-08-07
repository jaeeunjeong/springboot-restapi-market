package com.practice.springbootrestapimarket.factory.entity;

import com.practice.springbootrestapimarket.entity.category.Category;
import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.post.Image;
import com.practice.springbootrestapimarket.entity.post.Post;

import java.util.Arrays;
import java.util.List;

import static com.practice.springbootrestapimarket.factory.entity.CategoryFactory.createCategory;
import static com.practice.springbootrestapimarket.factory.entity.MemberFactory.createMember;

public class PostFactory {
    public static Post createPost() {
        return createPost(createMember(), createCategory());
    }

    public static Post createPost(Member member, Category category) {
        return new Post("갤럭시 22 울트라 팝니다.", "거의 새거!", 8_000_000L, member, category, Arrays.asList());
    }

    public static Post createPostWithImageWithoutMemberAndCategory(List<Image> images) {
        return new Post("갤럭시 22 팝니다.", "거의 새거!", 8_000_000L, createMember(), createCategory(), Arrays.asList());
    }

    public static Post createPostWithImage(Member member, Category category, List<Image> images) {
        return new Post("스포츠 스타 줌 팝니다!", "거의 새거!", 160_000L, member, category, images);
    }
}