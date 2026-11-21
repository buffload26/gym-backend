package com.backend.gym.shared.exception.auth;

import com.backend.gym.shared.exception.domain.AuthException;

public class InvalidVerificationCodeException extends AuthException {
    public InvalidVerificationCodeException() {
        super("Invalid verification code");
    }
}
