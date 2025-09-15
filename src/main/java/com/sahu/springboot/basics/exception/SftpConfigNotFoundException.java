package com.sahu.springboot.basics.exception;

public class SftpConfigNotFoundException extends RuntimeException {
    public SftpConfigNotFoundException(String message) {
        super(message);
    }
}
