package com.practice.springbootrestapimarket.controller.member;

import com.practice.springbootrestapimarket.dto.sign.SignInRequest;
import com.practice.springbootrestapimarket.dto.sign.SignInResponse;
import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.exception.MemberNotFoundException;
import com.practice.springbootrestapimarket.init.TestInitDB;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import com.practice.springbootrestapimarket.service.sign.SignService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 통합 테스트를 위함
@AutoConfigureMockMvc // 테스트에는 실제로 빈이 등록되지 않기떄문에 주입을 위해 필요
@ActiveProfiles("test") // 테스트 환경임을 명시
@Transactional // 데이터베이스를 사용하기에 필요
public class MemberControllerIntegrationTest {

    @Autowired
    WebApplicationContext webApplicationContext; // 빌드를 위해 필요
    @Autowired
    MockMvc mockMvc; // 가상으로 API들을 사용하기 위함
    @Autowired
    TestInitDB testInitDB; // 통합테스트에 필요한 DB들을 주입받기 위함
    @Autowired
    SignService signService;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
        testInitDB.initDB();
    }

    // 사용자 정보 조회
    @Test
    void readTest() throws Exception {
        // given : 계정 찾아서 읽어오기
        Member member = memberRepository.findByEmail(testInitDB.getUser1Email()).orElseThrow(MemberNotFoundException::new);

        // when,  then
        mockMvc.perform(
                get("/api/members/{id}", member.getId()))
                .andExpect(status().isOk());
    }

    // 사용자 정보 삭제 (권한은...)
    @Test
    void deleteByUserTest() throws Exception {
        // given 멤버 만들어서 회원 정보를 가져오고 로그인까지 완료하기..
        Member member = memberRepository.findByEmail(testInitDB.getUser1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse response = signService.signIn(new SignInRequest(testInitDB.getUser1Email(), testInitDB.getPassword()));

        // when, then :
        mockMvc.perform(
                delete("/api/members/{id}",
                        member.getId())
                        .header("Authorization", response.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
    void deleteByAdminTest() throws Exception {
        // given : 멤버 만들고 로그인 완료하기.
        Member member = memberRepository.findByEmail(testInitDB.getUser1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse signInRes = signService.signIn(new SignInRequest(testInitDB.getAdminEmail(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId())
                        .header("Authorization", signInRes.getAccessToken()))
                .andExpect(status().isOk());
    }

    @Test
        // 토큰이 없는 사용자가 지울 경우 -> 로그인 처리 하지 않음
    void deleteUnauthorizedByNoneTokenTest() throws Exception {
        // given -> 로그인 처리가 되지 않아서 토큰이 없음!
        Member member = memberRepository.findByEmail(testInitDB.getUser1Email()).orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

    @Test
        // 인증된 사용자가 다른 사람의 자원에 접근을 요청한 경우
    void deleteAccessDeniedByResourceOwnerTest() throws Exception {
        // given  member : 현재 생성된 자원, signInRes : 인증된 사용자
        Member member = memberRepository.findByEmail(testInitDB.getUser1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse otherUser = signService.signIn(new SignInRequest(testInitDB.getUser2Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/members/{id}", member.getId())
                        .header("Authorization", otherUser.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @Test
        // access 토큰이 아닌 refresh 토큰으로 요청한 경우 -> 인증이 안 됨.
    void deleteByRefreshToken() throws Exception {
        // given
        Member member = memberRepository.findByEmail(testInitDB.getUser1Email()).orElseThrow(MemberNotFoundException::new);
        SignInResponse signInRes = signService.signIn(new SignInRequest(testInitDB.getUser1Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                delete("/api/memberes/{id}", member.getId())
                        .header("Authorization", signInRes.getRefreshToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }
}
