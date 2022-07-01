package com.practice.springbootrestapimarket.service.category;

import com.practice.springbootrestapimarket.dto.category.CategoryCreateRequest;
import com.practice.springbootrestapimarket.dto.category.CategoryDto;
import com.practice.springbootrestapimarket.exception.CategoryNotFoundException;
import com.practice.springbootrestapimarket.factory.entity.CategoryFactory;
import com.practice.springbootrestapimarket.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    CategoryRepository categoryRepository;
    @InjectMocks
    CategoryService categoryService;


    @Test
    void readAllTest() {
        // given
        given(categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc())
                .willReturn(
                        Arrays.asList(CategoryFactory.createCategoryWithName("category1"),
                                CategoryFactory.createCategoryWithName("category2"))
                );

        // when
        List<CategoryDto> result = categoryService.readAll();

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName()).isEqualTo("category1");
        assertThat(result.get(1).getName()).isEqualTo("category2");
    }

    @Test
    void createTest() {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest();

        // when
        categoryService.create(req);

        // then
        verify(categoryRepository).save(any());
    }

    @Test
    void deleteTest() {
        // given
        given(categoryRepository.existsById(anyLong())).willReturn(true);

        // when
        categoryService.delete(1L);

        // then
        verify(categoryRepository).deleteById(anyLong());
    }

    @DisplayName("카테고리를 찾을 수 없어서 에러가 발생하는 경우")
    @Test
    void delete2Test() {
        // given
        given(categoryRepository.existsById(anyLong())).willReturn(false);

        // when, then
        assertThatThrownBy(()-> categoryService.delete(1L)).isInstanceOf(CategoryNotFoundException.class);
    }
}