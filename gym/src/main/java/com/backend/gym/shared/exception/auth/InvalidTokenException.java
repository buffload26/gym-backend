package com.backend.gym.shared.exception.auth;

import com.backend.gym.shared.exception.domain.BusinessException;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super("Invalid or expired token");
    }
}