package com.backend.gym.workout.application.port.in;

import java.util.UUID;

import com.backend.gym.workout.domain.WorkoutExercise;

public interface UpdateWorkoutExerciseUseCase {
    WorkoutExercise execute(UpdateWorkoutExerciseCommand command);

    record UpdateWorkoutExerciseCommand(
        UUID id,
        Integer position,
        Integer targetSets,
        String targetReps,
        String notes,
        Integer sortOrder
    ) {}
}
