package com.backend.gym.exercise.domain;

import com.backend.gym.user.domain.User;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExerciseFavorite(
    UUID id,
    User user,
    Exercise exercise,
    LocalDateTime createdAt
) {}