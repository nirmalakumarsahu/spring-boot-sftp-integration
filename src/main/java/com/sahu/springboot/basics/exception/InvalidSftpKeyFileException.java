package com.sahu.springboot.basics.exception;

public class InvalidSftpKeyFileException extends RuntimeException {
    public InvalidSftpKeyFileException(String message) {
        super(message);
    }
}
