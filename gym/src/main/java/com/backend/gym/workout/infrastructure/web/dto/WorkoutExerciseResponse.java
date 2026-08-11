package com.backend.gym.workout.infrastructure.web.dto;

import java.util.UUID;

import com.backend.gym.exercise.infrastructure.web.dto.ExerciseWithLoadsResponse;
import com.backend.gym.workout.domain.WorkoutExercise;

public record WorkoutExerciseResponse(
    UUID id,
    WorkoutResponse workout,
    ExerciseWithLoadsResponse exercise,
    Integer position,
    Integer targetSets,
    String targetReps,
    String notes,
    Integer sortOrder
) {
    public static WorkoutExerciseResponse fromDomain(WorkoutExercise workoutExercise) {
    return new WorkoutExerciseResponse(
        workoutExercise.id(),
        WorkoutResponse.fromDomain(workoutExercise.workout()),
        ExerciseWithLoadsResponse.fromDomain(workoutExercise.exercise()),
        workoutExercise.position(),
        workoutExercise.targetSets(),
        workoutExercise.targetReps(),
        workoutExercise.notes(),
        workoutExercise.sortOrder()
    );
}
}