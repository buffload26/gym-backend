package com.backend.gym.exercise.infrastructure.web.dto;

import java.util.UUID;

public record CreateExerciseRequest(
    String name,
    String description,
    String muscleGroup,
    String imageUrl,
    String videoUrl,
    boolean isDefault,
    UUID createdByUserId
) {}