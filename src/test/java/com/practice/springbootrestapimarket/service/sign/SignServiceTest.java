package com.practice.springbootrestapimarket.service.sign;


import com.practice.springbootrestapimarket.config.config.TokenHelper;
import com.practice.springbootrestapimarket.dto.sign.RefreshTokenResponse;
import com.practice.springbootrestapimarket.dto.sign.SignInRequest;
import com.practice.springbootrestapimarket.dto.sign.SignInResponse;
import com.practice.springbootrestapimarket.dto.sign.SignUpRequest;
import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.member.Role;
import com.practice.springbootrestapimarket.entity.member.RoleType;
import com.practice.springbootrestapimarket.exception.*;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import com.practice.springbootrestapimarket.repository.role.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

import static com.practice.springbootrestapimarket.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static com.practice.springbootrestapimarket.factory.entity.MemberFactory.createMember;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class SignServiceTest {

    SignService signService;
    @Mock
    MemberRepository memberRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    RoleRepository roleRepository;
    @Mock
    TokenHelper accessTokenHelper;
    @Mock
    TokenHelper refreshTokenHelper;

    @BeforeEach
    void beforeEach(){
        signService = new SignService(memberRepository, roleRepository, passwordEncoder, accessTokenHelper, refreshTokenHelper);
    }

    @DisplayName("????????????")
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


    @DisplayName("?????? ???????????? ????????? ??????")
    @Test
    void test2() {
        // given
        BDDMockito.given(memberRepository.existsByEmail(BDDMockito.anyString()))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberEmailAlreadyExistsException.class);
    }

    @DisplayName("?????? ???????????? ????????? ??????")
    @Test
    void test3() {
        // given
        BDDMockito.given(memberRepository.existsByNickname(BDDMockito.anyString()))
                .willReturn(true);

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberNicknameAlreadyExistsException.class);
    }

    @DisplayName("???????????? ?????? ???????????? ????????????")
    @Test
    void test4() {
        // given roleRepository??? ?????? ????????? null??? ??????.
        BDDMockito.given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signUp(createSignUpRequest()))
                .isInstanceOf(RoleNotFoundException.class);
    }

    @DisplayName("?????????")
    @Test
    void test5() {
        // given email, password, accessToken, refreshToken
        BDDMockito.given(memberRepository.findByEmail(BDDMockito.any())).willReturn(Optional.of(createMember()));
        BDDMockito.given(passwordEncoder.matches(BDDMockito.anyString(), BDDMockito.anyString())).willReturn(true);
        BDDMockito.given(accessTokenHelper.createToken(BDDMockito.anyString())).willReturn("access");
        BDDMockito.given(refreshTokenHelper.createToken(BDDMockito.anyString())).willReturn("refresh");

        // when
        SignInResponse res = signService.signIn(new SignInRequest("email", "password"));

        // then
        Assertions.assertThat(res.getAccessToken()).isEqualTo("access");
        Assertions.assertThat(res.getRefreshToken()).isEqualTo("refresh");
    }

    @DisplayName("????????? ???????????? ?????? ?????? ?????????")
    @Test
    void test6() {
        // given
        BDDMockito.given(memberRepository.findByEmail(BDDMockito.any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @DisplayName("?????? ????????? ?????? ??????")
    @Test
    void test7() {
        // given
        BDDMockito.given(memberRepository.findByEmail(BDDMockito.any())).willReturn(Optional.of(createMember()));
        BDDMockito.given(passwordEncoder.matches(BDDMockito.anyString(), BDDMockito.anyString())).willReturn(false);

        // when, then
        assertThatThrownBy(() -> signService.signIn(new SignInRequest("email", "password")))
                .isInstanceOf(LoginFailureException.class);
    }

    @DisplayName("access token??? ??????????????? refresh token?????? ????????? ??? access token ?????? ????????????.")
    @Test
    void test8() {
        // given
        String refreshToken = "refreshToken";
        String subject = "subject";
        String accessToken = "accessToken";
        BDDMockito.given(refreshTokenHelper.validate(refreshToken)).willReturn(true);
        BDDMockito.given(refreshTokenHelper.extractSubject(refreshToken)).willReturn(subject);
        BDDMockito.given(accessTokenHelper.createToken(subject)).willReturn(accessToken);

        // when
        RefreshTokenResponse res = signService.refreshToken(refreshToken);

        // then
        Assertions.assertThat(res.getAccessToken()).isEqualTo(accessToken);

    }
    @DisplayName("access token??? ??????????????? refresh token?????? ????????????????????? ???????????? ?????? ??????")
    @Test
    void test9() {
        // given
        String refreshToken = "refreshToken";
        BDDMockito.given(refreshTokenHelper.validate(refreshToken)).willReturn(false);

        // when, then
        assertThatThrownBy(()->signService.refreshToken(refreshToken))
                .isInstanceOf(AuthenticationEntryPointException.class);
    }
}
