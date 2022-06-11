package com.practice.springbootrestapimarket.factory.dto;

import com.practice.springbootrestapimarket.dto.sign.SignUpRequest;

public class SignUpRequestFactory {

    public static SignUpRequest createSignUpRequest() {
        return new SignUpRequest("jejeong@email", "1!2@3#4$", "eugene", "nicky");
    }

    public static SignUpRequest createSignUpRequest(String email, String password, String username, String nickname) {
        return new SignUpRequest("jejeong@email", "1!2@3#4$", "eugene", "nicky");
    }

    public static SignUpRequest createSignUpRequestWithEmail(String email) {
        return new SignUpRequest(email, "1!2@3#4$", "eugene", "nicky");
    }

    public static SignUpRequest createSignUpRequestWithPassword(String password) {
        return new SignUpRequest("jejeong@email", password, "eugene", "nicky");
    }

    public static SignUpRequest createSignUpRequestWithUsername(String username) {
        return new SignUpRequest("jejeong@email", "1!2@3#4$", username, "nicky");
    }

    public static SignUpRequest createSignUpRequestWithNickname(String nickname) {
        return new SignUpRequest("jejeong@email", "1!2@3#4$", "eugene", nickname);
    }
}
