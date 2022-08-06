package com.practice.springbootrestapimarket.entity;

import com.practice.springbootrestapimarket.entity.category.Category;
import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.post.Image;
import com.practice.springbootrestapimarket.entity.post.Post;
import com.practice.springbootrestapimarket.exception.UnsupportedImageFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.practice.springbootrestapimarket.factory.entity.CategoryFactory.createCategory;
import static com.practice.springbootrestapimarket.factory.entity.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ImageTest {

    @DisplayName("이미지 잘 들어오는지 확인")
    @Test
    void test1() {
        // given
        String name = "hello";
        String extension = "jpeg";

        // when, then
        Image image = new Image(name + "." + extension);
    }

    @DisplayName("확장자가 잘못 되었을 때 에러 발생 확인")
    @Test
    void test2() {
        // given
        String name = "hello";
        String extension = "invalid";

        // when, then
        assertThatThrownBy(() -> new Image(name + "." + extension)).isInstanceOf(UnsupportedImageFormatException.class);
    }

    @DisplayName("확장자가 없을 때 에러 발생 확인")
    @Test
    void test3() {
        // given
        String name = "hello";

        // when, then
        assertThatThrownBy(
                () -> new Image(name))
                .isInstanceOf(UnsupportedImageFormatException.class);
    }

    @DisplayName("initPost 확인하기")
    @Test
    void test4() {
        // given
        Image image = new Image("hello.jpg");
        Member member = createMember();
        Category category = createCategory();

        // when
        Post post = new Post("Gram 판매합니다.", 1_600_000L, member, category, Arrays.asList());
        image.initPost(post);

        // then
        assertThat(image.getPost()).isSameAs(post);
    }

    @DisplayName("post의 이미지들 구분하기")
    @Test
    void test5() {
        // given
        Member member = createMember();
        Category category = createCategory();
        Image image = new Image("hello.jpg");
        Post post1 = new Post("Gram 판매합니다.", 1_600_000L, member, category, Arrays.asList());
        image.initPost(post1);

        // when
        Post post2 = new Post("Gram 판매합니다.", 1_600_000L, member, category, Arrays.asList());
        image.initPost(post2);

        // then
        assertThat(image.getPost()).isNotSameAs(post2);
    }
}
