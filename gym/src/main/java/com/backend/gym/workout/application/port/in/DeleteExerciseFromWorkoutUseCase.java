package com.backend.gym.workout.application.port.in;

import java.util.UUID;

public interface DeleteExerciseFromWorkoutUseCase {
    void execute(UUID id);
}