package com.backend.gym.exercise.application.port.out;

import com.backend.gym.exercise.domain.ExerciseFavorite;

import java.util.List;
import java.util.UUID;

public interface ExerciseFavoriteRepositoryPort {
    ExerciseFavorite save(UUID userId, UUID exerciseId);
    void delete(UUID userId, UUID exerciseId);
    List<ExerciseFavorite> findAllByUserId(UUID userId);
    boolean existsByUserIdAndExerciseId(UUID userId, UUID exerciseId);
}