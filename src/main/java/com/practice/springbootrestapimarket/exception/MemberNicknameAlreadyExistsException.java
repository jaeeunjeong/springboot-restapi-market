package com.practice.springbootrestapimarket.exception;

public class MemberNicknameAlreadyExistsException extends RuntimeException {
    public MemberNicknameAlreadyExistsException(String nickname) {
    }
}
