package com.backend.gym.shared.exception.auth;

import com.backend.gym.shared.exception.domain.AuthException;

public class VerificationCodeExpiredException extends AuthException {
    public VerificationCodeExpiredException() {
        super("Verification code has expired");
    }
}
