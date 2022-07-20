package com.practice.springbootrestapimarket;

import com.practice.springbootrestapimarket.entity.category.Category;
import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.member.Role;
import com.practice.springbootrestapimarket.entity.member.RoleType;
import com.practice.springbootrestapimarket.exception.MemberNotFoundException;
import com.practice.springbootrestapimarket.exception.RoleNotFoundException;
import com.practice.springbootrestapimarket.repository.category.CategoryRepository;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import com.practice.springbootrestapimarket.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("local")
public class InitDB {

    private final RoleRepository roleRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    private String adminEmail = "admin@shop.com";
    private String user1Email = "user1@shop.com";
    private String user2Email = "user2@shop.com";
    private String password = "1!2@3#4$a";

    @Transactional
    public void initDB() {
        log.info("Initializer Database");
        initRole();
        initTestAdmin();
        initTestMember();
        initCategory();
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

    private void initCategory() {
        Category c1 = categoryRepository.save(new Category("category1", null));
        Category c2 = categoryRepository.save(new Category("category2", c1));
        Category c3 = categoryRepository.save(new Category("category3", c1));
        Category c4 = categoryRepository.save(new Category("category4", c2));
        Category c5 = categoryRepository.save(new Category("category5", c2));
        Category c6 = categoryRepository.save(new Category("category6", c4));
        Category c7 = categoryRepository.save(new Category("category7", c3));
        Category c8 = categoryRepository.save(new Category("category8", null));
    }
}
