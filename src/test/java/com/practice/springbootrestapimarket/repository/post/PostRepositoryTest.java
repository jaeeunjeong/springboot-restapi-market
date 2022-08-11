package com.practice.springbootrestapimarket.repository.post;

import com.practice.springbootrestapimarket.entity.category.Category;
import com.practice.springbootrestapimarket.entity.member.Member;
import com.practice.springbootrestapimarket.entity.post.Image;
import com.practice.springbootrestapimarket.entity.post.Post;
import com.practice.springbootrestapimarket.exception.PostNotFoundException;
import com.practice.springbootrestapimarket.repository.category.CategoryRepository;
import com.practice.springbootrestapimarket.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.practice.springbootrestapimarket.factory.entity.CategoryFactory.createCategory;
import static com.practice.springbootrestapimarket.factory.entity.ImageFactory.createImage;
import static com.practice.springbootrestapimarket.factory.entity.MemberFactory.createMember;
import static com.practice.springbootrestapimarket.factory.entity.PostFactory.createPost;
import static com.practice.springbootrestapimarket.factory.entity.PostFactory.createPostWithImage;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    ImageRepository imageRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @PersistenceContext
    EntityManager em;

    Member member;
    Category category;

    @BeforeEach
    void beforeEach() {
        member = memberRepository.save(createMember());
        category = categoryRepository.save(createCategory());
    }

    private void clear() {
        em.flush();
        em.clear();
    }

    @DisplayName("Post 생성과 조회")
    @Test
    void test1() {
        // given
        Post post = createPost(member, category);
        postRepository.save(post);
        clear();

        // when
        Post result = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);

        // then
        assertThat(result.getId()).isEqualTo(post.getId());
        assertThat(result.getContent()).isEqualTo(post.getContent());
    }

    @DisplayName("Post 삭제")
    @Test
    void test2() {
        // given
        Post post = postRepository.save(createPost(member, category));
        clear();

        // when
        postRepository.deleteById(post.getId());
        clear();

        // then
        assertThatThrownBy(() -> postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new))
                .isInstanceOf(PostNotFoundException.class);

    }

    @DisplayName("이미지 연쇄 생성 확인")
    @Test
    void test3() {
        // given
        Post post = postRepository.save(createPostWithImage(member, category, Arrays.asList(createImage(), createImage())));
        clear();

        // when
        Post result = postRepository.findById(post.getId()).orElseThrow(PostNotFoundException::new);

        // then
        List<Image> resultImage = result.getImages();
        assertThat(post.getImages().size()).isEqualTo(2);
    }

    @DisplayName("이미지 연쇄 제거 확인")
    @Test
    void test4() {
        // given
        Post post = postRepository.save(createPostWithImage(member, category, Arrays.asList(createImage(), createImage())));
        clear();

        // when
        postRepository.deleteById(post.getId());
        clear();

        // then -> 이미지
        List<Image> list = imageRepository.findAll();
        assertThat(list.size()).isZero();
    }

    @DisplayName("멤버가 제거되었을 떄, 연쇄 제거 확인")
    @Test
    void test5() {
        // given
        List<Image> images = new ArrayList<>();
        images.add(createImage());
        images.add(createImage());
        Post post = postRepository.save(createPostWithImage(member, category, images));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        List<Image> list = imageRepository.findAll();
        assertThat(list.size()).isZero();
    }

    @DisplayName("카테고리가 제거되었을 때, 연쇄 제거 확인")
    @Test
    void test6() {
        // given
        List<Image> images = new ArrayList<>(Arrays.asList(createImage(), createImage()));
        Post post = postRepository.save(createPostWithImage(member, category, images));
        clear();

        // when
        categoryRepository.deleteById(category.getId());
        clear();

        // then
        List<Image> list = imageRepository.findAll();
        assertThat(list.size()).isZero();
    }
}
