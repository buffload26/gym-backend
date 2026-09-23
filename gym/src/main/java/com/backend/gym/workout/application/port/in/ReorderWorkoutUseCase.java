package com.backend.gym.workout.application.port.in;

import java.util.UUID;

public interface ReorderWorkoutUseCase {
    void execute(ReorderWorkoutCommand command);

    record ReorderWorkoutCommand(
        UUID workoutId,
        int newSortOrder
    ) {}
}
