package com.practice.springbootrestapimarket.repository.member;

import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.member.Role;
import com.practice.springbootrestapimarket.exception.MemberNotFoundException;
import com.practice.springbootrestapimarket.repository.role.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;
    @PersistenceContext
    EntityManager em;

    private Member createMemberWithRoles(List<Role> roles) {
        return new Member("email", "password", "username", "nickname", roles);
    }

    private Member createMember(String email, String password, String username, String nickname) {
        return new Member(email, password, username, nickname, Collections.emptyList());
    }

    private Member createMember() {
        return new Member("email", "password", "username", "nickname", Collections.emptyList());
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    @Test
    void 회원가입_및_조회() {
        //given : 멤버를 만든다.
        Member member = createMember();

        //when : 회원 가입
        memberRepository.save(member);
        clear();

        //then 조회로 검증증
        Member result = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        Assertions.assertThat(result.getId()).isEqualTo(member.getId());
    }

    @Test
    void EntityDate_확인() {
        //given : 멤버 객체 생성
        Member member = createMember();

        //when : 회원 가입 하기
        memberRepository.save(member);
        clear();

        //cretedAt과 modifiedAt이 null 아닌지 확인 및 생성시점과 수정 시점이 동일한지 확인.
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        Assertions.assertThat(foundMember.getCreatedAt()).isNotNull();
        Assertions.assertThat(foundMember.getModifiedAt()).isNotNull();
        Assertions.assertThat(foundMember.getCreatedAt()).isEqualTo(foundMember.getModifiedAt());
    }
}
