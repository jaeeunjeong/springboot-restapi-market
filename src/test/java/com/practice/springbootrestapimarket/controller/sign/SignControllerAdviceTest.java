package com.practice.springbootrestapimarket.controller.sign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbootrestapimarket.advice.ExceptionAdvice;
import com.practice.springbootrestapimarket.exception.AuthenticationEntryPointException;
import com.practice.springbootrestapimarket.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class SignControllerAdviceTest {

    @InjectMocks SignController signController;
    @Mock SignService signService;
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(signController).setControllerAdvice(new ExceptionAdvice()).build();
    }

    @Test
    @DisplayName("토큰이 아예 없는 경우 refresh token 확인")
    void test1() throws Exception {
        // given
        given(signService.refreshToken(anyString())).willThrow(AuthenticationEntryPointException.class);

        // when, then
        mockMvc.perform(
                post("/api/refresh-token")
                        .header("Authorization", "refreshToken"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.code").value(-1001));
    }

    @Test
    @DisplayName("누락된 HTTP 요청 헤더로 인한 예외 발생")
    void test2() throws Exception {
        // given, when, then
        mockMvc.perform(
                post("/api/refresh-token"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(-1009));
    }
}
