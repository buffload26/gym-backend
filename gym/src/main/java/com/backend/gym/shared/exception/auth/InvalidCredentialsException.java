package com.backend.gym.shared.exception.auth;

import com.backend.gym.shared.exception.domain.AuthException;

public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}