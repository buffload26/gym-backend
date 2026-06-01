package com.backend.gym.exercise.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.user.domain.User;

public record Exercise(
    UUID id,
    String name,
    String description,
    String muscleGroup,
    String imageUrl,
    String videoUrl,
    boolean isDefault,
    User createdByUser,  
    LocalDateTime deletedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}