package com.example.demo_jwt.exceptions;

public class RefreshTokenMissingException extends RuntimeException{
    public RefreshTokenMissingException(String msg) {
        super(msg);
    }

    public RefreshTokenMissingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
