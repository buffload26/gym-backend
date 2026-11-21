package com.backend.gym.shared.exception.domain;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}