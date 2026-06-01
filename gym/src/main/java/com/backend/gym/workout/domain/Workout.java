package com.backend.gym.workout.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.user.domain.User;

public record Workout(
    UUID id,
    User user,    
    String name,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}
