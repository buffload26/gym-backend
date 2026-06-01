package com.backend.gym.exercise.application.port.in;

import java.util.UUID;

public interface SoftDeleteExerciseUseCase {
    void execute(UUID id);
}
