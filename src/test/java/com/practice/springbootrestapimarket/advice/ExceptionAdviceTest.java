package com.practice.springbootrestapimarket.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbootrestapimarket.controller.sign.SignController;
import com.practice.springbootrestapimarket.dto.sign.SignInRequest;
import com.practice.springbootrestapimarket.dto.sign.SignUpRequest;
import com.practice.springbootrestapimarket.exception.*;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ExceptionAdviceTest {

    @InjectMocks
    SignController signController;
    @Mock
    SignService signService;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(signController)
                .setControllerAdvice(new ExceptionAdvice()).build();
    }

    @Test
    void signInMethodArgumentNotValidExceptionTest() throws Exception{
        // given
        SignInRequest req = new SignInRequest("email", "1234");

        // when, then
        mockMvc.perform(
                post("/api/signIn")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req))
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    void signUpMethodArgumentNotValidExceptionTest() throws Exception {
        // given
        SignUpRequest req = new SignUpRequest("", "", "", "");

        // when, then
        mockMvc.perform(
                post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginFailureExceptionTest() throws Exception {
        // given
        SignInRequest req = new SignInRequest("email@com", "password");
        given(signService.signIn(any())).willThrow(LoginFailureException.class);
        // when, then
        mockMvc.perform(
                post("/api/signIn")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void memberEmailAlreadyExistsExceptionTest() throws Exception {
        // given
        SignUpRequest req = new SignUpRequest("email@com", "password", "usin", "nicky");
        doThrow(MemberEmailAlreadyExistsException.class).when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void memberNicknameAlreadyExistsExceptionTest() throws Exception {
        // given
        SignUpRequest req = new SignUpRequest("email@com", "password", "usin", "nicky");
        doThrow(MemberNicknameAlreadyExistsException.class).when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void memberNotFoundExceptionTest() throws Exception {
        // given
        SignUpRequest req = new SignUpRequest("email@com", "password", "usin", "nicky");
        doThrow(MemberNotFoundException.class).when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void roleNotFoundExceptionTest() throws Exception {
        // given
        SignUpRequest req = new SignUpRequest("email@net", "password1!2@", "eugean", "nicky");
        doThrow(RoleNotFoundException.class).when(signService).signUp(any());

        // when, then
        mockMvc.perform(
                post("/api/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }
}