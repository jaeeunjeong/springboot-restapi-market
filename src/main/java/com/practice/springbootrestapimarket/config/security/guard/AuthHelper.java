package com.practice.springbootrestapimarket.config.security.guard;

import com.practice.springbootrestapimarket.config.security.CustomAuthenticationToken;
import com.practice.springbootrestapimarket.config.security.CustomUserDetails;
import com.practice.springbootrestapimarket.entity.member.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class AuthHelper {
    public boolean isAuthenticated() {
        return false;
    }

    public Long extractMemberId() {
        return Long.valueOf(getUserDetails().getUserId());
    }

    public Set<RoleType> extractMemberRoles() {
        return null;
    }

//    public boolean isAccessTokenType() {
//        return "access".equals(((CustomAuthenticationToken) getAuthentication()).getType());
//    }
//
//    public boolean isRefreshTokenType() {
//        return "refresh".equals(((CustomAuthenticationToken) getAuthentication()).getType());
//    }

    private CustomUserDetails getUserDetails() {
        return (CustomUserDetails) getAuthentication().getPrincipal();
    }

    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
