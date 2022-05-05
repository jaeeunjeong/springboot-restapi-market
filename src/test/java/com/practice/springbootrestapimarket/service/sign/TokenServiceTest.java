package com.practice.springbootrestapimarket.service.sign;

import com.practice.springbootrestapimarket.handler.JwtHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @Mock
    JwtHandler jwtHandler;
    @InjectMocks
    TokenService tokenService;

    @BeforeEach
    void beforeEach() {
        ReflectionTestUtils.setField(tokenService, "accessTokenMaxAgeSecond", 10L);
        ReflectionTestUtils.setField(tokenService, "refreshTokenMaxAgeSecond", 10L);
        ReflectionTestUtils.setField(tokenService, "accessKey", "accessKey");
        ReflectionTestUtils.setField(tokenService, "refreshKey", "refreshKey");
    }

    @Test
    void makeAccessTokenTest() {
        // given
        given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("access");

        // when
        String token = tokenService.makeAccessToken("access");

        // then
        Assertions.assertThat(token).isEqualTo("access");
        verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
    }

    @Test
    void makeRefreshTokenTest() {
        // given
        given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("refresh");

        // when
        String token = tokenService.makeRefreshToken("refresh");

        // then
        Assertions.assertThat(token).isEqualTo("refresh");
        verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
    }
}