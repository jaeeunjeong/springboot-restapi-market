package com.practice.springbootrestapimarket.repository.post;

import com.practice.springbootrestapimarket.entity.post.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
