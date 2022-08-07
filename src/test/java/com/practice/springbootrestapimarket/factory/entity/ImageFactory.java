package com.practice.springbootrestapimarket.factory.entity;

import com.practice.springbootrestapimarket.entity.post.Image;

public class ImageFactory {
    public static Image createImage() {
        return new Image("photo1.jpg");
    }

    public static Image createImageWithName(String name) {
        return new Image(name);
    }
}
