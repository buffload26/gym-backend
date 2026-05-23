package com.backend.gym.user.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record User(
    UUID id,
    String name,
    String email,
    String passwordHash,
    String verificationCode,
    LocalDateTime verificationCodeExpiresAt,
    boolean isVerified,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
