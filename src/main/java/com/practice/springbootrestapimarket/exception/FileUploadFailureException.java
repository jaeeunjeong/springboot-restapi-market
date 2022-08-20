package com.practice.springbootrestapimarket.exception;

public class FileUploadFailureException extends RuntimeException {
    public FileUploadFailureException(Throwable t) {
        super(t);
    }
}