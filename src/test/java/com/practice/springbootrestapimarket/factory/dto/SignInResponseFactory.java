package com.practice.springbootrestapimarket.factory.dto;

import com.practice.springbootrestapimarket.dto.sign.SignInResponse;

public class SignInResponseFactory {
    public SignInResponse createSignInRequest(String accessToken, String refreshToken){
        return new SignInResponse(accessToken, refreshToken);
    }
}
