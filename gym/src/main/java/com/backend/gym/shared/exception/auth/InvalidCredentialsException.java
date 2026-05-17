package com.backend.gym.shared.exception.auth;

import com.backend.gym.shared.exception.domain.BusinessException;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}