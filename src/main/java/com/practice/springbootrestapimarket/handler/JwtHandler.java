package com.practice.springbootrestapimarket.handler;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtHandler {

    private String type = "Bearer";

    /**
     * 토큰 생성
     * @param encodedKey
     * @param subject
     * @param maxAgeSeconds
     * @return
     */
    public String createToken(String encodedKey, String subject, long maxAgeSeconds) {
        Date now = new Date();
        return type + Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + maxAgeSeconds * 1000L))
                .signWith(SignatureAlgorithm.HS256, encodedKey)
                .compact();
    }

    /**
     * subject를 추출하기 위한 메서드 (사용자 인증에 사용)
     * @param encodingKey
     * @param token
     * @return
     */
    public String extractSubject(String encodingKey, String token) {
        return parse(encodingKey, token).getBody().getSubject();
    }

    /**
     * 유효성을 검증하기 위한 메서드
     * @param encodingKey
     * @param token
     * @return
     */
    public boolean validate(String encodingKey, String token) {
        try {
            parse(encodingKey, token);
            return true;
        } catch (JwtException jwtException) {
            return false;
        }
    }

    private Jws<Claims> parse(String encodingKey, String token) {
        return Jwts.parser()
                .setSigningKey(encodingKey)
                .parseClaimsJws(originTokenData(token));
    }

    /**
     * 토큰에는 토큰의 타입도 지정되어있는데 실제 데이터만 가지고 오기 위한 메서드
     */
    private String originTokenData(String token) {
        return token.substring(type.length());
    }
}
