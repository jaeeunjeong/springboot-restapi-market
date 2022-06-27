package com.practice.springbootrestapimarket.config.security.guard;

import com.practice.springbootrestapimarket.entity.member.RoleType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class MemberGuard {

    private AuthHelper authHelper;

    // AuthHelper를 이용해서 인증되었는지, 엑세스 토큰을 통한 요청인지 자원 접근 권한을 갖고 있는 지를 검사
//    public boolean check(Long id){
//        return authHelper.isAuthenticated() && authHelper.isAccessTokenType() && hasAuthority(id);
//    }

    public boolean hasAuthority(Long id) {
        Long memberId = authHelper.extractMemberId();
        Set<RoleType> memberRoles = authHelper.extractMemberRoles();
        return id.equals(memberId) || memberRoles.contains(RoleType.ROLE_ADMIN);
    }
}
