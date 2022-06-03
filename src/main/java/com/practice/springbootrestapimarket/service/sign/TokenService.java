package com.practice.springbootrestapimarket.service.sign;

import com.practice.springbootrestapimarket.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 로그인시에만 사용되기때문에 로그인 과정과 동일한 패키지에 작성
 */
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtHandler jwtHandler;

    @Value("${jwt.max-age.access}")
    private long accessTokenMaxAgeSecond;

    @Value("${jwt.max-age.refresh}")
    private long refreshTokenMaxAgeSecond;

    @Value("${jwt.key.access}")
    private String accessKey;

    @Value("{$jwt.key.refresh}")
    private String refreshKey;

    public String makeAccessToken(String subject) {
        return jwtHandler.createToken(accessKey, subject, accessTokenMaxAgeSecond);
    }

    public String makeRefreshToken(String subject) {
        return jwtHandler.createToken(accessKey, subject, refreshTokenMaxAgeSecond);
    }

    public boolean validateAccessToken(String token){
        return jwtHandler.validate(accessKey, token);
    }

    public boolean validateRefreshToken(String token){
        return jwtHandler.validate(refreshKey, token);
    }

    public String extractAccessTokenSubject(String token){
        return jwtHandler.extractSubject(accessKey, token);
    }

    public String extractRefreshTokenSubject(String token){
        return jwtHandler.extractSubject(refreshKey, token);
    }
}
