package com.backend.gym.loadentry.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.user.domain.User;
import com.backend.gym.workout.domain.Workout;

public record LoadEntry(
    UUID id,
    User user,
    Exercise exercise,
    Workout workout,
    LocalDate performedAt,
    BigDecimal loadKg,
    Integer sets,
    Integer reps,
    String notes,
    LocalDateTime createdAt
) {}
