package com.backend.gym.workout.application.port.in;

import java.util.UUID;

public interface ReorderWorkoutExerciseUseCase {
    void execute(ReorderWorkoutExerciseCommand command);

    record ReorderWorkoutExerciseCommand(
        UUID workoutExerciseId,
        int newSortOrder
    ) {}
}