package com.practice.springbootrestapimarket.service.sign;


import com.practice.springbootrestapimarket.dto.sign.SignInRequest;
import com.practice.springbootrestapimarket.dto.sign.SignInResponse;
import com.practice.springbootrestapimarket.dto.sign.SignUpRequest;
import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.member.Role;
import com.practice.springbootrestapimarket.entity.member.RoleType;
import com.practice.springbootrestapimarket.exception.LoginFailureException;
import com.practice.springbootrestapimarket.exception.MemberEmailAlreadyExistsException;
import com.practice.springbootrestapimarket.exception.MemberNicknameAlreadyExistsException;
import com.practice.springbootrestapimarket.exception.RoleNotFoundException;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import com.practice.springbootrestapimarket.repository.role.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SignServiceTest {

    @InjectMocks
    SignService signService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    TokenService tokenService;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RoleRepository roleRepository;

    @DisplayName("회원가입")
    @Test
    void test1() {
        // given
        SignUpRequest req = createSignUpRequest();
        BDDMockito.given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.of(new Role((RoleType.ROLE_NORMAL))));

        // when
        signService.signUp(req);

        // then
        BDDMockito.verify(passwordEncoder).encode(req.getPassword());
        BDDMockito.verify(memberRepository).save(BDDMockito.any());
    }


    @DisplayName("중복 이메일이 있는지 확인")
    @Test
    void test2() {
        // given
        BDDMockito.given(memberRepository.existsByEmail(BDDMockito.anyString()))
                .willReturn(true);

        // when, then
        Assertions.assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberEmailAlreadyExistsException.class);
    }

    @DisplayName("중복 닉네임이 있는지 확인")
    @Test
    void test3() {
        // given
        BDDMockito.given(memberRepository.existsByNickname(BDDMockito.anyString()))
                .willReturn(true);

        // when, then
        Assertions.assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberNicknameAlreadyExistsException.class);
    }

    @DisplayName("등록되지 않은 권한인지 확인하기")
    @Test
    void test4() {
        // given roleRepository의 값을 임의로 null값 넣기.
        BDDMockito.given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(RoleNotFoundException.class);
    }

    @DisplayName("로그인")
    @Test
    void test5() {
        // given email, password, accessToken, refreshToken
        BDDMockito.given(memberRepository.findByEmail(BDDMockito.any())).willReturn(Optional.of(createMember()));
        BDDMockito.given(passwordEncoder.matches(BDDMockito.anyString(), BDDMockito.anyString())).willReturn(true);
        BDDMockito.given(tokenService.makeAccessToken(BDDMockito.anyString())).willReturn("access");
        BDDMockito.given(tokenService.makeRefreshToken(BDDMockito.anyString())).willReturn("refresh");

        // when
        SignInResponse res = signService.signIn(new SignInRequest("email", "password"));

        // then
        Assertions.assertThat(res.getAccessToken()).isEqualTo("access");
        Assertions.assertThat(res.getRefreshToken()).isEqualTo("refresh");
    }

    @DisplayName("등록된 이메일이 아닌 경우 로그인")
    @Test
    void test6() {
        // given
        BDDMockito.given(memberRepository.findByEmail(BDDMockito.any())).willReturn(Optional.empty());

        // when, then
        Assertions.assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @DisplayName("비밀 번호가 틀린 경우")
    @Test
    void test7() {
        // given
        BDDMockito.given(memberRepository.findByEmail(BDDMockito.any())).willReturn(Optional.of(createMember()));
        BDDMockito.given(passwordEncoder.matches(BDDMockito.anyString(), BDDMockito.anyString())).willReturn(false);

        // when, then
        Assertions.assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    private Member createMember() {
        return new Member("email", "password", "jejeong", "jjam", Collections.emptyList());
    }

    private SignUpRequest createSignUpRequest() {
        return new SignUpRequest("email", "password", "name", "nickname");
    }

}
