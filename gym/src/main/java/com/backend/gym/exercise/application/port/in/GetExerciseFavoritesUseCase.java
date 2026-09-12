package com.backend.gym.exercise.application.port.in;

import java.util.List;
import java.util.UUID;

import com.backend.gym.exercise.domain.ExerciseFavorite;

public interface GetExerciseFavoritesUseCase {
    List<ExerciseFavorite> execute(UUID userId);
}
