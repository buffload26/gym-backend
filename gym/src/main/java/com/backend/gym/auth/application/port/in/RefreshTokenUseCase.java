package com.backend.gym.auth.application.port.in;

import com.backend.gym.auth.domain.AuthResult;

public interface RefreshTokenUseCase {
    AuthResult execute(String refreshToken);
}