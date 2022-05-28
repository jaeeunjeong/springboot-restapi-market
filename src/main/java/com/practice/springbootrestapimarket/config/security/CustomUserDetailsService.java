package com.practice.springbootrestapimarket.config.security;

import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // 인증된 사용자 정보를 반환해준다.
    @Override
    public CustomUserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findById(Long.valueOf(userId)).orElseGet(() -> new Member(null, null, null, null, Arrays.asList()));
        return new CustomUserDetails(
                String.valueOf(member.getId()),
                member.getRoles().stream().map(memberRole -> memberRole.getRole())
                        .map(role -> role.getRoleType())
                        .map(roleType -> roleType.toString())
                        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet())
        );
    }
}
