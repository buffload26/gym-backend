package com.backend.gym.user.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public record User(
    UUID id,
    String name,
    String email,
    String passwordHash,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
