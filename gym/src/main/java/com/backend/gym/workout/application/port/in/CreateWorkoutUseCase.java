package com.backend.gym.workout.application.port.in;

import java.util.UUID;

import com.backend.gym.workout.domain.Workout;

public interface CreateWorkoutUseCase {
    Workout execute(CreateWorkoutCommand command);

    record CreateWorkoutCommand(
        UUID userId,
        String name,
        String description
    ) {}
}
