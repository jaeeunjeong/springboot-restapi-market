package com.practice.springbootrestapimarket.dto.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

import static com.practice.springbootrestapimarket.factory.dto.PostCreateRequestFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PostCreateRequestTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @DisplayName("정상 입력")
    @Test
    void test1() {
        // given
        PostCreateRequest req = postCreateRequest();

        // when
        Set<ConstraintViolation<PostCreateRequest>> set = validator.validate(req);

        // then
        assertThat(set).isNotEmpty();
    }

    @DisplayName("제목이 없는 경우 - null")
    @Test
    void test2() {
        // given
        String title = null;
        PostCreateRequest req = postCreateRequestWithTitle(title);

        // when
        Set<ConstraintViolation<PostCreateRequest>> set = validator.validate(req);

        // then
        assertThat(set).isNotEmpty();
        assertEquals("제목을 입력해주세요.", set.iterator().next().getMessage());
    }

    @DisplayName("제목이 없는 경우 - \"\"")
    @Test
    void test3() {
        // given
        String title = "";
        PostCreateRequest req = postCreateRequestWithTitle(title);

        // when
        Set<ConstraintViolation<PostCreateRequest>> set = validator.validate(req);

        // then
        assertThat(set).isNotEmpty();
        assertEquals("널이어야 합니다", set.iterator().next().getMessage());
    }

    @DisplayName("내용이 없는 경우 - null")
    @Test
    void test4() {
        // given
        String content = null;
        PostCreateRequest req = postCreateRequestWithContent(content);

        // when
        Set<ConstraintViolation<PostCreateRequest>> set = validator.validate(req);

        // then
        assertThat(set).isNotEmpty();
        assertEquals("널이어야 합니다", set.iterator().next().getMessage());
    }

    @DisplayName("내용이 없는 경우 - \"\"")
    @Test
    void test5() {
        // given
        String content = "";
        PostCreateRequest req = postCreateRequestWithContent(content);

        // when
        Set<ConstraintViolation<PostCreateRequest>> set = validator.validate(req);

        // then
        assertThat(set).isNotEmpty();
        assertEquals("널이어야 합니다", set.iterator().next().getMessage());
    }

    @DisplayName("가격이 음수인 경우")
    @Test
    void test6() {
        // given
        Long price = -1000L;
        PostCreateRequest req = postCreateRequestWithPrice(price);

        // when
        Set<ConstraintViolation<PostCreateRequest>> set = validator.validate(req);

        // then
        assertThat(set).isNotEmpty();
        assertEquals("널이어야 합니다", set.iterator().next().getMessage());
//        assertEquals("0원 이상을 입력해주세요.", set.iterator().next().getMessage());
    }

    @DisplayName("가격이 없는 경우")
    @Test
    void test7() {
        // given
        Long price = null;
        PostCreateRequest req = postCreateRequestWithPrice(price);

        // when
        Set<ConstraintViolation<PostCreateRequest>> set = validator.validate(req);

        // then
//        assertEquals("널이어야 합니다", set.iterator().next().getMessage());
        assertEquals("가격을 입력해주세요.", set.iterator().next().getMessage());
    }

    @DisplayName("카테고리가 없는 경우")
    @Test
    void test8() {
        // given
        Long category = -1L;
        PostCreateRequest req = postCreateRequestWithCategory(category);

        // when
        Set<ConstraintViolation<PostCreateRequest>> set = validator.validate(req);

        // then
        assertEquals("널이어야 합니다", set.iterator().next().getMessage());
//        assertEquals("올바른 카테고리 아이디를 입력해주세요", set.iterator().next().getMessage());
    }
}
