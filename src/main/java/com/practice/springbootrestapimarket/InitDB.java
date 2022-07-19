package com.practice.springbootrestapimarket;

import com.practice.springbootrestapimarket.entity.category.Category;
import com.practice.springbootrestapimarket.entity.member.Role;
import com.practice.springbootrestapimarket.entity.member.RoleType;
import com.practice.springbootrestapimarket.repository.category.CategoryRepository;
import com.practice.springbootrestapimarket.repository.role.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
@RequiredArgsConstructor
@Profile("local")
public class InitDB {

    private final RoleRepository roleRepository;
    private final CategoryRepository categoryRepository;

    @PostConstruct
    public void initDB() {
        log.info("Initializer Database");
        initRole();
    }

    private void initRole() {
        roleRepository.saveAll(Stream.of(RoleType.values()).map(roleType -> new Role(roleType)).collect(Collectors.toList()));
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
