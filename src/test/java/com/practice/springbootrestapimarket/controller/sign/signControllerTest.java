package com.practice.springbootrestapimarket.controller.sign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbootrestapimarket.dto.sign.RefreshTokenResponse;
import com.practice.springbootrestapimarket.dto.sign.SignInRequest;
import com.practice.springbootrestapimarket.dto.sign.SignInResponse;
import com.practice.springbootrestapimarket.dto.sign.SignUpRequest;
import com.practice.springbootrestapimarket.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static com.practice.springbootrestapimarket.factory.dto.SignInRequestFactory.createSignInRequest;
import static com.practice.springbootrestapimarket.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class signControllerTest {

    @InjectMocks
    SignController signController;
    @Mock
    SignService signService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(signController).build();
    }

    @Test
    void signInTest() throws Exception {
        // given
        SignInRequest req = createSignInRequest("email@email.com", "123456a!");
        given(signService.signIn(req)).willReturn(new SignInResponse("access", "refresh"));

        // when, then
        mockMvc.perform(
                post("/api/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.accessToken").value("access")) // 3
                .andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));

        verify(signService).signIn(req);
    }

    @Test
    void signUpTest() throws Exception {
        // given
        SignUpRequest req = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");

        // when, then
        mockMvc.perform(
                post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))) // 2
                .andExpect(status().isCreated());

        verify(signService).signUp(req);
    }

    @Test
    void refreshTokenTest() throws Exception{
        // given
        given(signService.refreshToken("refreshToken")).willReturn(new RefreshTokenResponse("accessToken"));

        // when, then
        mockMvc.perform(
                post("/api/refresh-token")
                .header("Authorization", "refreshToken")
        ).andExpect(status().isOk())
        .andExpect(jsonPath("$.result.data.accessToken").value("accessToken"));
    }
}