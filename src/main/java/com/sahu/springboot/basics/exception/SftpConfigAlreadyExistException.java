package com.sahu.springboot.basics.exception;

public class SftpConfigAlreadyExistException extends RuntimeException {
    public SftpConfigAlreadyExistException(String message) {
        super(message);
    }
}
