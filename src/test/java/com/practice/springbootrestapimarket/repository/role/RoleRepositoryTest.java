package com.practice.springbootrestapimarket.repository.role;

import com.practice.springbootrestapimarket.entity.member.Role;
import com.practice.springbootrestapimarket.entity.member.RoleType;
import com.practice.springbootrestapimarket.exception.RoleNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    RoleRepository roleRepository;
    @PersistenceContext
    EntityManager em;

    // crud....?

    @Test
    void createAndSelect() {
        // given
        Role role = createRole();

        // when
        roleRepository.save(role);
        clear();

        //then -> 아이디로 결과 찾고 비교해서 확인!
        Role result = roleRepository.findById(role.getId()).orElseThrow(RoleNotFoundException::new);
        Assertions.assertThat(result.getRoleType()).isEqualTo(role.getRoleType());
    }

    @Test
    void role_삭제() {
        //given
        Role role = roleRepository.save(createRole());
        clear();

        // when
        roleRepository.delete(role);

        // then
        Assertions.assertThatThrownBy(() -> roleRepository.findById(role.getId()).orElseThrow(RoleNotFoundException::new)).isInstanceOf(RoleNotFoundException.class);
    }

    @Test
    void role_Unique_확인() {
        // given
        Role role = roleRepository.save(createRole());
        clear();

        // when and then
        Assertions.assertThat(roleRepository.existsById(role.getId())).isTrue();
        Assertions.assertThat(roleRepository.existsById(role.getId() + 1)).isFalse();

        Assertions.assertThatThrownBy(() -> roleRepository.save(createRole())).isInstanceOf(DataIntegrityViolationException.class);

    }

    private Role createRole() {
        return new Role(RoleType.ROLE_NORMAL);
    }

    private void clear() {
        em.flush();
        em.clear();
    }
}
