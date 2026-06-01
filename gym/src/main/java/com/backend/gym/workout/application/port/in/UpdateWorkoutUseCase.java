package com.backend.gym.workout.application.port.in;

import java.util.UUID;

import com.backend.gym.workout.domain.Workout;

public interface UpdateWorkoutUseCase {
    Workout execute(UpdateWorkoutCommand command);

    record UpdateWorkoutCommand(
        UUID id,
        String name,
        String description
    ) {}
}
