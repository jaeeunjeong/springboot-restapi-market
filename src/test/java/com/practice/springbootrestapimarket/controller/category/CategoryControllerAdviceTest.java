package com.practice.springbootrestapimarket.controller.category;

import com.practice.springbootrestapimarket.advice.ExceptionAdvice;
import com.practice.springbootrestapimarket.exception.CannotConvertNestedStructureException;
import com.practice.springbootrestapimarket.exception.CategoryNotFoundException;
import com.practice.springbootrestapimarket.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerAdviceTest {

    @InjectMocks
    CategoryController categoryController;
    @Mock
    CategoryService categoryService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).setControllerAdvice(new ExceptionAdvice()).build();
    }

    @DisplayName("카테고리 목록 조회시 카테고리가 정상이 아닌 경우.")
    @Test
    void test1() throws Exception {
        // given
        given(categoryService.readAll()).willThrow(CannotConvertNestedStructureException.class);

        // when, then
        mockMvc.perform(
                get("/api/categories"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(-1011));
    }

    @DisplayName("지울 카테고리가 적절하지 않은 경우")
    @Test
    void test2() throws Exception {
        // given
        doThrow(CategoryNotFoundException.class).when(categoryService).delete(anyLong());

        // when, then
        mockMvc.perform(
                delete("/api/categories/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(-1010));
    }

}