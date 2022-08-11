package com.practice.springbootrestapimarket.repository.post;

import com.practice.springbootrestapimarket.entity.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
