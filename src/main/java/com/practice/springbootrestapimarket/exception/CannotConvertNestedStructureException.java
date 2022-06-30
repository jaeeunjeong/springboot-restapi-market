package com.practice.springbootrestapimarket.exception;

public class CannotConvertNestedStructureException extends RuntimeException{
    public CannotConvertNestedStructureException(String message) {
        super(message);
    }
}