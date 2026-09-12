package com.backend.gym.exercise.application.port.in;

import java.util.UUID;

public interface ToggleExerciseFavoriteUseCase {
    void execute(ToggleExerciseFavoriteCommand command);

    record ToggleExerciseFavoriteCommand(UUID userId, UUID exerciseId) {}
}
