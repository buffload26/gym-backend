package com.backend.gym.shared.exception.auth;

import com.backend.gym.shared.exception.domain.BusinessException;

public class VerificationCodeExpiredException extends BusinessException {
    public VerificationCodeExpiredException() {
        super("Verification code has expired");
    }
}
