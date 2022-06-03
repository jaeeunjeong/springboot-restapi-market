package com.practice.springbootrestapimarket.init;

import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.member.Role;
import com.practice.springbootrestapimarket.entity.member.RoleType;
import com.practice.springbootrestapimarket.exception.MemberNotFoundException;
import com.practice.springbootrestapimarket.exception.RoleNotFoundException;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import com.practice.springbootrestapimarket.repository.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

/*
 * 테스트 초기화를 위한 클래스
 */
@Component
public class TestInitDB {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    private String adminEmail = "admin@shop.com";
    private String user1Email = "user1@shop.com";
    private String user2Email = "user2@shop.com";
    private String password = "1!2@3#4$";

    @Transactional
    public void initDB() {
        initRole();
        initTestAdmin();
        initTestMember();
    }

    private void initRole() {
        roleRepository.saveAll(
                Arrays.stream(RoleType.values())
                        .map(roleType -> new Role(roleType))
                        .collect(Collectors.toList())
        );
    }

    // ADMIN한테는 권한 여러개 주기(일반 권한, 관리자 권한)
    private void initTestAdmin() {
        memberRepository.save(
                new Member(adminEmail, passwordEncoder.encode(password), "ADMIN", "ADMIN",
                        Arrays.asList(
                                roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                                roleRepository.findByRoleType(RoleType.ROLE_ADMIN).orElseThrow(RoleNotFoundException::new)
                        )
                )
        );
    }

    private void initTestMember() {
        memberRepository.saveAll(
          Arrays.asList(
                  new Member(user1Email, passwordEncoder.encode(password), "user1", "user1",
                          Arrays.asList(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(MemberNotFoundException::new))),
                  new Member(user2Email, passwordEncoder.encode(password), "user2", "user2",
                          Arrays.asList(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(MemberNotFoundException::new)))
          )
        );
    }

    // 여기는 lombok가 안 되는 것 같음 ->

    public String getAdminEmail() {
        return adminEmail;
    }

    public String getUser1Email() {
        return user1Email;
    }

    public String getUser2Email() {
        return user2Email;
    }

    public String getPassword() {
        return password;
    }
}
