package com.backend.gym.exercise.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.user.infrastructure.web.dto.UserResponse;

public record ExerciseResponse(
    UUID id,
    String name,
    String description,
    String muscleGroup,
    String imageUrl,
    String videoUrl,
    boolean isDefault,
    boolean isFavorite,
    UserResponse createdByUser,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static ExerciseResponse fromDomain(Exercise exercise) {
        return new ExerciseResponse(
            exercise.id(),
            exercise.name(),
            exercise.description(),
            exercise.muscleGroup(),
            exercise.imageUrl(),
            exercise.videoUrl(),
            exercise.isDefault(),
            exercise.isFavorite(),
            exercise.createdByUser() != null ? UserResponse.fromDomain(exercise.createdByUser()) : null,
            exercise.createdAt(),
            exercise.updatedAt()
        );
    }
}