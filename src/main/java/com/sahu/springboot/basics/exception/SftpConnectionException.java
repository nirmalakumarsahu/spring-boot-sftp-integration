package com.sahu.springboot.basics.exception;

public class SftpConnectionException extends RuntimeException {
    public SftpConnectionException(String message) {
        super(message);
    }
}
