package com.practice.springbootrestapimarket.service.sign;

import com.practice.springbootrestapimarket.dto.sign.SignInRequest;
import com.practice.springbootrestapimarket.dto.sign.SignInResponse;
import com.practice.springbootrestapimarket.dto.sign.SignUpRequest;
import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.member.RoleType;
import com.practice.springbootrestapimarket.exception.LoginFailureException;
import com.practice.springbootrestapimarket.exception.MemberEmailAlreadyExistsException;
import com.practice.springbootrestapimarket.exception.MemberNicknameAlreadyExistsException;
import com.practice.springbootrestapimarket.exception.RoleNotFoundException;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import com.practice.springbootrestapimarket.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    /**
     * 회원 가입
     *
     * @param req
     */
    @Transactional
    public void signup(SignUpRequest req) {
        validateSignUpInfo(req);
        memberRepository.save(
                SignUpRequest.toEntity(
                        req,
                        roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                        passwordEncoder));
    }

    /**
     * 로그인 처리
     *
     * @param req
     * @return
     */
    public SignInResponse signIn(SignInRequest req) {
        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
        validatePassword(req, member);
        String subject = createSubject(member);
        String accessToken = tokenService.makeAccessToken(subject);
        String refreshToken = tokenService.makeRefreshToken(subject);
        return new SignInResponse(accessToken, refreshToken);
    }

    /**
     * 회원 가입 시 이메일/ 닉네임 중복 검증
     */
    private void validateSignUpInfo(SignUpRequest req) {
        if (memberRepository.existsByEmail(req.getEmail()))
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        if (memberRepository.existsByNickname(req.getNickname()))
            throw new MemberNicknameAlreadyExistsException(req.getNickname());
    }

    /**
     * 요청 정보와 회원의 비밀번호를 검증
     */
    private void validatePassword(SignInRequest req, Member member) {
        if (passwordEncoder.matches(req.getPassword(), member.getPassword())) {
            throw new LoginFailureException();
        }
    }

    /**
     * 사용자 가입하기.
     *
     * @return
     */
    private String createSubject(Member member) {
        return String.valueOf(member.getId());
    }
}