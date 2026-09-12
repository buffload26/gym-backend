package com.backend.gym.exercise.infrastructure.web.dto;

import com.backend.gym.exercise.domain.ExerciseFavorite;

import java.time.LocalDateTime;
import java.util.UUID;

public record ExerciseFavoriteResponse(
    UUID id,
    ExerciseResponse exercise,
    LocalDateTime createdAt
) {
    public static ExerciseFavoriteResponse fromDomain(ExerciseFavorite favorite) {
        return new ExerciseFavoriteResponse(
            favorite.id(),
            ExerciseResponse.fromDomain(favorite.exercise()),
            favorite.createdAt()
        );
    }
}