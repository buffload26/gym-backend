package com.backend.gym.shared.exception.auth;

import com.backend.gym.shared.exception.domain.BusinessException;

public class InvalidVerificationCodeException extends BusinessException {
    public InvalidVerificationCodeException() {
        super("Invalid verification code");
    }
}
