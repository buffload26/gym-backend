package com.backend.gym.auth.infrastructure.web.dto;

import com.backend.gym.auth.domain.TokenPair;

public record TokenResponse(String accessToken, String refreshToken) {
    public static TokenResponse fromDomain(TokenPair pair) {
        return new TokenResponse(pair.accessToken(), pair.refreshToken());
    }
}
