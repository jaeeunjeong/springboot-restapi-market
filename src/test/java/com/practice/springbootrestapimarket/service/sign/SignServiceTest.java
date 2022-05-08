package com.practice.springbootrestapimarket.service.sign;


import com.practice.springbootrestapimarket.dto.sign.SignUpRequest;
import com.practice.springbootrestapimarket.entity.member.Role;
import com.practice.springbootrestapimarket.entity.member.RoleType;
import com.practice.springbootrestapimarket.exception.MemberEmailAlreadyExistsException;
import com.practice.springbootrestapimarket.exception.MemberNicknameAlreadyExistsException;
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


    private SignUpRequest createSignUpRequest() {
        return new SignUpRequest("email", "password", "name", "nickname");
    }

}
