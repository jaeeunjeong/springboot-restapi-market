package com.practice.springbootrestapimarket.config.security;

import com.practice.springbootrestapimarket.config.config.TokenHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final TokenHelper accessTokenHelper;
    private final CustomUserDetailsService customUserDetailsService;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = extractToken(request);
        if (validateAccessToken(token)) {
            setAccessAuthentication("access", token);
        }
        chain.doFilter(request, response);
    }

    private String extractToken(ServletRequest request) {
        return ((HttpServletRequest) request).getHeader("Authorization");
    }

    // 토큰 검증하기
    private boolean validateAccessToken(String token) {
        return token != null && accessTokenHelper.validate(token);
    }

    private void setAccessAuthentication(String type, String token) {
        String userId = accessTokenHelper.extractSubject(token);
        CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }
}
