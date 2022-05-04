package com.practice.springbootrestapimarket.exception;

public class MemberEmailAlreadyExistsException extends RuntimeException {
    public MemberEmailAlreadyExistsException(String email) {
    }
}
