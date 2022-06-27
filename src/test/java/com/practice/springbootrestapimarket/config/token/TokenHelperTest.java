package com.practice.springbootrestapimarket.config.token;

import com.practice.springbootrestapimarket.config.config.TokenHelper;
import com.practice.springbootrestapimarket.handler.JwtHandler;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TokenHelperTest {

    TokenHelper tokenHelper;
    @Mock
    JwtHandler jwtHandler;

    @BeforeEach
    void beforeEach() {
        tokenHelper = new TokenHelper(jwtHandler, "Key", 1000L);
    }

    @Test
    void createTokenTest() {
        // given
        given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("token");

        // when
        String createdToken = tokenHelper.createToken("subject");

        // then
        Assertions.assertThat(createdToken).isEqualTo("token");
        verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
    }

    @Test
    void validateTest() {
        // given
        given(jwtHandler.validate(anyString(), anyString())).willReturn(true);

        // when
        boolean result = tokenHelper.validate("token");

        // then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void invalidateTest() {
        // given
        given(jwtHandler.validate(anyString(), anyString())).willReturn(false);

        // when
        boolean result = tokenHelper.validate("token");
        // then
        Assertions.assertThat(result).isFalse();
    }

    @Test
    void extractSubjectTest() {
        // given
        given(jwtHandler.extractSubject(anyString(), anyString())).willReturn("subject");

        // when
        String subject = tokenHelper.extractSubject("token");

        // then
        Assertions.assertThat(subject).isEqualTo(subject);
    }
}