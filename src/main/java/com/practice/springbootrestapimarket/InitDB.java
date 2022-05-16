package com.practice.springbootrestapimarket;

import com.practice.springbootrestapimarket.entity.member.Role;
import com.practice.springbootrestapimarket.entity.member.RoleType;
import com.practice.springbootrestapimarket.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("local")
public class InitDB {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initDB(){
        log.info("Initializer Database");
        initRole();
    }

    private void initRole() {
        roleRepository.saveAll(Stream.of(RoleType.values()).map(roleType -> new Role(roleType)).collect(Collectors.toList()));
    }
}
