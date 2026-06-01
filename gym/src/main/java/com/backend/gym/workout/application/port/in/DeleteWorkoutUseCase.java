package com.backend.gym.workout.application.port.in;

import java.util.UUID;

public interface DeleteWorkoutUseCase {
    void execute(UUID id);
}
