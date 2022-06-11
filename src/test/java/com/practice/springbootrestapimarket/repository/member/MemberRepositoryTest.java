package com.practice.springbootrestapimarket.repository.member;

import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.member.MemberRole;
import com.practice.springbootrestapimarket.entity.member.Role;
import com.practice.springbootrestapimarket.entity.member.RoleType;
import com.practice.springbootrestapimarket.exception.MemberNotFoundException;
import com.practice.springbootrestapimarket.repository.role.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.practice.springbootrestapimarket.factory.entity.MemberFactory.createMember;
import static com.practice.springbootrestapimarket.factory.entity.MemberFactory.createMemberWithRoles;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoleRepository roleRepository;
    @PersistenceContext
    EntityManager em;

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

    @Test
    void update_확인하기() {
        //given
        Member member = memberRepository.save(createMember());
        String newNickname = "BRANDNEW name";
        clear();

        //when
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        foundMember.updateNickname(newNickname);
        clear();

        //then
        Member resultMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        Assertions.assertThat((resultMember.getNickname())).isEqualTo(newNickname);
    }

    @Test
    void delete_확인하기() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when
        memberRepository.delete(member);
        clear();

        //then : 테스트를 수행하고 예외가 발생했을 때, 적절한 예외가 발생했는지 확인하는 과정
        Assertions.assertThatThrownBy(() -> memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new))
                .isInstanceOf(MemberNotFoundException.class);

    }

    @Test
    void 이메일로_회원찾기() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when
        Member result = memberRepository.findByEmail(member.getEmail()).orElseThrow(MemberNotFoundException::new);

        //then
        Assertions.assertThat(member.getId()).isEqualTo(result.getId());
        Assertions.assertThat(member.getEmail()).isEqualTo(result.getEmail());
    }

    @Test
    void 닉네임으로_회원찾기() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when
        Member result = memberRepository.findByNickname(member.getNickname()).orElseThrow(MemberNotFoundException::new);

        //then
        Assertions.assertThat(result.getId()).isEqualTo(member.getId());
        Assertions.assertThat(result.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    void 이메일_중복확인() {
        // given
        String email = "email";
        Member member = memberRepository.save(createMember(email, "password1", "username1", "nickname"));
        clear();

        // when, then
        Assertions.assertThatThrownBy(() -> memberRepository.save(createMember(email, "password2", "username2", "nickname2")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 닉네임_중복확인() {
        // given
        Member member = memberRepository.save(createMember("email1", "password1", "username1", "nickname"));
        clear();

        // when, then
        Assertions.assertThatThrownBy(() -> memberRepository.save(createMember("email2", "password1", "username2", member.getNickname())))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void 이미_존재하는_이메일인지_확인하기() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when, then
        Assertions.assertThat(memberRepository.existsByEmail(member.getEmail())).isTrue();
        Assertions.assertThat(memberRepository.existsByEmail(member.getEmail() + "_new")).isFalse();
    }

    @Test
    void 이미_존재하는_닉네임인지_확인하기() {
        // given
        Member member = memberRepository.save(createMember());
        clear();

        // when, then
        Assertions.assertThat(memberRepository.existsByNickname(member.getNickname())).isTrue();
        Assertions.assertThat(memberRepository.existsByNickname(member.getNickname().concat("_new"))).isFalse();
    }

    @Test
    void memberRole_변경시_연달아_변경되는지_획인() {
        // given : 사용될 role을 DB에 저장
        List<RoleType> roleTypes = Stream.of(RoleType.ROLE_ADMIN, RoleType.ROLE_NORMAL, RoleType.ROLE_SPACIAL_BUYER).collect(Collectors.toList());
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
        clear();

        // 저장된 Role을 member의 생성 인자로 전달한다.
        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        // when
        Member result = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        Set<MemberRole> memberRoles = result.getRoles();

        // then
        Assertions.assertThat(memberRoles.size()).isEqualTo(roleTypes.size());
    }

    @Test
    void memberRole_제거시_연달아_제거되는지_확인() {
        // given
        List<RoleType> roleTypes = Stream.of(RoleType.ROLE_ADMIN, RoleType.ROLE_NORMAL, RoleType.ROLE_SPACIAL_BUYER).collect(Collectors.toList());
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
        clear();

        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        List<MemberRole> result = em.createQuery("select mr from MemberRole mr", MemberRole.class).getResultList();
        Assertions.assertThat(result.size()).isZero();
    }
}