package com.backend.gym.user.infrastructure.web.dto;

import com.backend.gym.user.domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
    UUID id,
    String name,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserResponse fromDomain(User user) {
        return new UserResponse(
            user.id(),
            user.name(),
            user.email(),
            user.createdAt(),
            user.updatedAt()
        );
    }
}