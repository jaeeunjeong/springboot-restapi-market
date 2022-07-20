package com.practice.springbootrestapimarket.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.springbootrestapimarket.dto.category.CategoryCreateRequest;
import com.practice.springbootrestapimarket.dto.sign.SignInResponse;
import com.practice.springbootrestapimarket.entity.category.Category;
import com.practice.springbootrestapimarket.init.TestInitDB;
import com.practice.springbootrestapimarket.repository.category.CategoryRepository;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import com.practice.springbootrestapimarket.service.sign.SignService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static com.practice.springbootrestapimarket.factory.dto.SignInRequestFactory.createSignInRequest;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
@Transactional
public class CategoryControllerIntegrationTest {

    @Autowired
    WebApplicationContext context;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestInitDB testInitDB;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    SignService signService;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        testInitDB.initDB();
    }

    @DisplayName("카테고리 읽기")
    @Test
    void test1() throws Exception {
        // given, when, then
        mockMvc.perform(
                get("/api/categories"))
                .andExpect(status().isOk());
    }

    @DisplayName("관리자가 카테고리를 만드는 경우")
    @Test
    void test2() throws Exception {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest("공지사항", null);
        SignInResponse signInResponse = signService.signIn(createSignInRequest(testInitDB.getAdminEmail(), testInitDB.getPassword()));
        List<Category> before = categoryRepository.findAll();

        // when, then
        mockMvc.perform(
                post("/api/categories")
                        .header("Authorization", signInResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
        List<Category> result = categoryRepository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(before.size() + 1);
    }

    @DisplayName("관리자가 아닌 사람이 카테고리를 만드는 경우 -> 일반 사용자인 경우")
    @Test
    void test3() throws Exception {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest("공지사항", null);
        SignInResponse signInResponse = signService.signIn(createSignInRequest(testInitDB.getUser1Email(), testInitDB.getPassword()));

        // when, then
        mockMvc.perform(
                post("/api/categories")
                        .header("Authorization", signInResponse.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @DisplayName("관리자가 아닌 사람이 카테고리를 만드는 경우 -> 회원이 아닌 경우")
    @Test
    void test4() throws Exception {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest("공지사항", null);

        // when, then
        mockMvc.perform(
                post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

    @DisplayName("카테고리 삭제하기")
    @Test
    void test5() throws Exception {
        // given
        SignInResponse signInResponse = signService.signIn(createSignInRequest(testInitDB.getAdminEmail(), testInitDB.getPassword()));
        Long id = categoryRepository.findAll().get(0).getId();

        // when, then
        mockMvc.perform(
                delete("/api/categories/{id}", id)
                        .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().isOk());

        List<Category> result = categoryRepository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(0);
    }

    @DisplayName("관리자가 아닌 사람이 카테고리를 삭제하는 경우 -> 일반 사용자")
    @Test
    void test6() throws Exception {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest("공지사항", null);
        SignInResponse signInResponse = signService.signIn(createSignInRequest(testInitDB.getUser1Email(), testInitDB.getPassword()));
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/categories/{id}", id)
                        .header("Authorization", signInResponse.getAccessToken()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/entry-point"));
    }

    @DisplayName("관리자가 아닌 사람이 카테고리를 삭제하는 경우 -> 회원이 아닌 경우")
    @Test
    void test7() throws Exception {
        // given
        CategoryCreateRequest req = new CategoryCreateRequest("공지사항", null);
        Long id = 1L;

        // when, then
        mockMvc.perform(
                delete("/api/categories/{id}", id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/exception/access-denied"));
    }

}
