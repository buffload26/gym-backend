package com.backend.gym.exercise.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.backend.gym.exercise.domain.ExerciseWithLoads;
import com.backend.gym.loadentry.infrastructure.web.dto.LoadEntrySummaryResponse;
import com.backend.gym.user.infrastructure.web.dto.UserResponse;

public record ExerciseWithLoadsResponse(
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
    LocalDateTime updatedAt,
    List<LoadEntrySummaryResponse> loads
) {
    public static ExerciseWithLoadsResponse fromDomain(ExerciseWithLoads domain) {
        var exercise = domain.exercise();
        return new ExerciseWithLoadsResponse(
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
            exercise.updatedAt(),
            domain.loads().stream().map(LoadEntrySummaryResponse::fromDomain).toList()
        );
    }
}