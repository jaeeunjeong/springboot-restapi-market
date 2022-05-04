package com.practice.springbootrestapimarket.service.sign;

import com.practice.springbootrestapimarket.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 로그인시에만 사용되기때문에 로그인 과정과 동일한 패키지에 작성
 */
@Service
@RequiredArgsConstructor
public class TokenService {
    private final JwtHandler jwtHandler;

    private long accessTokenMaxAgeSecond;
    private long refreshTokenMaxAgeSecond;
    private String accessKey;
    private String refreshKey;

    public String makeAccessToken(String subject) {
        return jwtHandler.createToken(accessKey, subject, accessTokenMaxAgeSecond);
    }

    public String makeRefreshToken(String subject) {
        return jwtHandler.createToken(accessKey, subject, refreshTokenMaxAgeSecond);
    }

}
