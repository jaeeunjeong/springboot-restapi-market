package com.practice.springbootrestapimarket.controller.sign;

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
        mockMvc = MockMvcBuilders.standaloneSetup(signController).build();
    }

//    @Test
//    @DisplayName("인증되지 않은 사용자의 로그인시 예외 처리")
//    void test1() throws Exception {
//        // given
//        given(signService.refreshToken(anyString())).willThrow(AuthenticationEntryPointException.class);
//
//        // when, then
//        mockMvc.perform(
//                post("/api/refresh-token")
//                        .header("Authorization", "refreshToken"))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.code").value(-1001));
//    }

    @Test
    @DisplayName("토큰이 아예 없는 경우 refreshtoke 확인")
    void test2() throws Exception {
        // given, when, then
        mockMvc.perform(
                post("/api/refresh-token")
                        .header("Authorization", "refreshToken"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(-1009));
    }
}
