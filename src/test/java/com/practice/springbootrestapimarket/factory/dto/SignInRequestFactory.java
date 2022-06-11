package com.practice.springbootrestapimarket.factory.dto;

import com.practice.springbootrestapimarket.dto.sign.SignInRequest;

public class SignInRequestFactory {
    public static SignInRequest createSignInRequest() {
        return new SignInRequest("email@com", "123456a!");
    }

    public static SignInRequest createSignInRequest(String email, String password) {
        return new SignInRequest(email, password);
    }

    public static SignInRequest createSignInRequestWithEmail(String email) {
        return new SignInRequest(email, "1!2@3#4$");
    }

    public static SignInRequest createSignInRequestWithPassword(String password) {
        return new SignInRequest("email@com", password);
    }
}
