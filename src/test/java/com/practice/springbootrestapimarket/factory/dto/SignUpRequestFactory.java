package com.practice.springbootrestapimarket.factory.dto;

import com.practice.springbootrestapimarket.dto.sign.SignUpRequest;

public class SignUpRequestFactory {

    public SignUpRequest createSignUpRequest() {
        return new SignUpRequest("jejeong@email", "1!2@3#4$", "eugene", "nicky");
    }
}
