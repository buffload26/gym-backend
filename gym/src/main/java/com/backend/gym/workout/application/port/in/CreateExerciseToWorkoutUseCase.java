package com.backend.gym.workout.application.port.in;

import java.util.UUID;

import com.backend.gym.workout.domain.WorkoutExercise;

public interface CreateExerciseToWorkoutUseCase {
    WorkoutExercise execute(CreateExerciseToWorkoutCommand command);

    record CreateExerciseToWorkoutCommand(
        UUID workoutId,
        UUID exerciseId,
        Integer position,
        Integer targetSets,
        String targetReps,
        String notes,
        Integer sortOrder
    ) {}
}
