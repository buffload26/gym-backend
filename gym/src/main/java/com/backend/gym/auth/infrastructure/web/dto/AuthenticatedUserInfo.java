package com.backend.gym.auth.infrastructure.web.dto;

import java.util.UUID;

import com.backend.gym.user.domain.User;

public record AuthenticatedUserInfo(
    UUID id,
    String name,
    String email,
    boolean isVerified
) {
    public static AuthenticatedUserInfo fromDomain(User user) {
        return new AuthenticatedUserInfo(
            user.id(),
            user.name(),
            user.email(),
            user.isVerified()
        );
    }
}
