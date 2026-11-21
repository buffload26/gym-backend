package com.backend.gym.shared.exception.auth;

import com.backend.gym.shared.exception.domain.AuthException;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException() {
        super("Invalid or expired token");
    }
}