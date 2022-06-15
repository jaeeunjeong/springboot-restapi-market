package com.practice.springbootrestapimarket.handler;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Base64;

class JwtHandlerTest {

    JwtHandler jwtHandler = new JwtHandler();

    @Test
    void createATokenTest() {
        // given, when
        String encodingKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String subject = "SUBJECT";
        long longMaxSecond = 300L;
        String token = createToken(encodingKey, subject, longMaxSecond);

        // then : 토큰이 생성되었다면 토큰의 타입이 토큰 안에 지정되어 있을 것.
        Assertions.assertThat(token.contains("Bearer"));
    }

    @Test
    void extractSubjectTest() {
        // given
        String encodingKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String subject = "SUBJECT";
        long longMaxSecond = 300L;
        String token = createToken(encodingKey, subject, longMaxSecond);

        // when
        String extractSubject = jwtHandler.extractSubject(encodingKey, token);

        // then
        Assertions.assertThat(extractSubject).isEqualTo(subject);
    }

    @Test
    void validateTest() {
        // given
        String encodingKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String subject = "SUBJECT";
        long longMaxSecond = 300L;
        String token = createToken(encodingKey, subject, longMaxSecond);

        // when
        boolean isValid = jwtHandler.validate(encodingKey, token);

        // then
        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    void 정보가_없는_유효하지_않은_토큰이라면() {
        // given
        String encodingKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String subject = "SUBJECT";
        String fakeSubject = "FAKE_SUBJECT";
        long longMaxSecond = 300L;
        String token = createToken(encodingKey, subject, longMaxSecond);

        // when
        boolean isValid = jwtHandler.validate(fakeSubject, token);

        // then
        Assertions.assertThat(isValid).isFalse();

    }

    @Test
    void 유효기간이_만료된_토큰이라면() {
        // given
        String encodingKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String subject = "SUBJECT";
        Long longMaxSecond = 0L;
        String token = createToken(encodingKey, subject, longMaxSecond);

        // when
        boolean isValid = jwtHandler.validate(encodingKey, token);

        // then
        Assertions.assertThat(isValid).isFalse();
    }

    public String createToken(String encodingKey, String subject, long longMaxSecond) {
        return jwtHandler.createToken(encodingKey, subject, longMaxSecond);
    }
}