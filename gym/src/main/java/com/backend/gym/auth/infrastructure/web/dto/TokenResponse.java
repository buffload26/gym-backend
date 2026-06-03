package com.backend.gym.auth.infrastructure.web.dto;

import com.backend.gym.auth.domain.TokenPair;
import com.backend.gym.user.domain.User;

public record TokenResponse(
    String accessToken,
    String refreshToken,
    AuthenticatedUserInfo user
) {
    public static TokenResponse fromDomain(TokenPair pair, User user) {
        return new TokenResponse(
            pair.accessToken(),
            pair.refreshToken(),
            AuthenticatedUserInfo.fromDomain(user)
        );
    }
}
