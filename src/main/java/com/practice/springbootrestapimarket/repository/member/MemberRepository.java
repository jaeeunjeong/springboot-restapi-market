package com.practice.springbootrestapimarket.repository.member;

import com.practice.springbootrestapimarket.entity.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    @EntityGraph("Member.roles")
    Optional<Member> findWithRolesById(Long id);
}
