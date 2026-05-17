package com.backend.gym.auth.application.port.in;

import com.backend.gym.auth.domain.TokenPair;

public interface RefreshTokenUseCase {
    TokenPair execute(String refreshToken);
}