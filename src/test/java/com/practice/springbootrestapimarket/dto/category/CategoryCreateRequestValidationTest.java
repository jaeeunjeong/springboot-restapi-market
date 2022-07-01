package com.practice.springbootrestapimarket.dto.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import static com.practice.springbootrestapimarket.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequest;
import static com.practice.springbootrestapimarket.factory.dto.CategoryCreateRequestFactory.createCategoryCreateRequestWithName;
import static org.assertj.core.api.Assertions.assertThat;

public class CategoryCreateRequestValidationTest {

    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateTest(){
        // given
        CategoryCreateRequest req = createCategoryCreateRequest();

        // when
        Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isEmpty();
    }

    @DisplayName("이름이 너무 짧은 경우")
    @Test
    void validateTest2(){
        // given
        String invalidValue = "a";
        CategoryCreateRequest req = createCategoryCreateRequestWithName(invalidValue);

        // when
        Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v-> v.getInvalidValue()).collect(Collectors.toSet()).contains(invalidValue));
    }

    @DisplayName("이름이 너무 긴 경우")
    @Test
    void validateTest3(){
        // given
        String name = "cccccccccccccccccccccccccccccccccccccccccccccccccc";// c 50개
        CategoryCreateRequest req = createCategoryCreateRequestWithName(name);

        // when
        Set<ConstraintViolation<CategoryCreateRequest>> validate = validator.validate(req);

        // then
        assertThat(validate).isNotEmpty();
        assertThat(validate.stream().map(v -> v.getInvalidValue()).collect(Collectors.toSet())).contains(name);
    }
}
