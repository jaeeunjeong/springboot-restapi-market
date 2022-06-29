package com.practice.springbootrestapimarket.repository.category;

import com.practice.springbootrestapimarket.entity.category.Category;
import com.practice.springbootrestapimarket.exception.CategoryNotFoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.practice.springbootrestapimarket.factory.entity.CategoryFactory.createCategoryWithName;
import static com.practice.springbootrestapimarket.factory.entity.CategoryFactory.createCategory;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;
    @PersistenceContext
    EntityManager em;

    @DisplayName("카테고리 생성 및 확인")
    @Test
    void test1() {
        // given
        Category category = createCategory();

        // when
        Category savedCategory = categoryRepository.save(category);
        clear();

        // then
        Category foundCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow(CategoryNotFoundException::new);
        Assertions.assertThat(foundCategory.getId()).isEqualTo(savedCategory.getId());
    }

    @DisplayName("전부 읽어오기")
    @Test
    void test2() {
        // given
        List<Category> categories = Arrays.asList("name1", "name2","name3").stream().map(n-> createCategoryWithName(n)).collect(Collectors.toList());
        categoryRepository.saveAll(categories);
        clear();

        // when
        List<Category> foundCategories = categoryRepository.findAll();

        // then
        Assertions.assertThat(foundCategories.size()).isEqualTo(categories.size());
    }


    @DisplayName("지우는 경우")
    @Test
    void test3() {
        // given
        Category category = categoryRepository.save(createCategory());
        clear();

        // when
        categoryRepository.delete(category);
        clear();

        // then
        Assertions.assertThatThrownBy(()-> categoryRepository.findById(category.getId()).orElseThrow(CategoryNotFoundException::new)).isInstanceOf(CategoryNotFoundException.class);
    }

    @DisplayName("연달아서 지워지게 하기.")
    @Test
    void test4() {
        // given
        Category category1 = categoryRepository.save(createCategoryWithName("category1"));
        Category category2 = categoryRepository.save(createCategory("category2", category1));
        Category category3 = categoryRepository.save(createCategory("category3", category2));
        Category category4 = categoryRepository.save(createCategoryWithName("category4"));
        clear();

        // when
        categoryRepository.deleteById(category1.getId());
        clear();

        // then
        List<Category> result = categoryRepository.findAll();
        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(category4.getId());
    }

    @DisplayName("데이터가 없는 경우")
    @Test
    void test5() {
        // given
        Long fakeId = 1000L;

        // when, then
        Assertions.assertThatThrownBy(()->categoryRepository.deleteById(fakeId)).isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("부모부터 쭉 카테고리 찾기")
    @Test
    void test6() {
        // given
        Category category1 = categoryRepository.save(createCategory("category1", null));
        Category category2 = categoryRepository.save(createCategory("category2", category1));
        Category category3 = categoryRepository.save(createCategory("category3", category1));
        Category category4 = categoryRepository.save(createCategory("category4", category2));
        Category category5 = categoryRepository.save(createCategory("category1", category2));
        Category category6 = categoryRepository.save(createCategory("category2", category4));
        Category category7 = categoryRepository.save(createCategory("category3", category3));
        Category category8 = categoryRepository.save(createCategory("category4", null));

        // when
        List<Category> result = categoryRepository.findAllOrderByParentIdAscNullsFirstCategoryIdAsc();

        // then
        Assertions.assertThat(result.size()).isEqualTo(8);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(category1.getId());
        Assertions.assertThat(result.get(1).getId()).isEqualTo(category8.getId());
        Assertions.assertThat(result.get(2).getId()).isEqualTo(category2.getId());
        Assertions.assertThat(result.get(3).getId()).isEqualTo(category3.getId());
        Assertions.assertThat(result.get(4).getId()).isEqualTo(category4.getId());
        Assertions.assertThat(result.get(5).getId()).isEqualTo(category5.getId());
        Assertions.assertThat(result.get(6).getId()).isEqualTo(category7.getId());
        Assertions.assertThat(result.get(7).getId()).isEqualTo(category6.getId());
    }

    void clear() {
        em.flush();
        em.clear();
    }
}
