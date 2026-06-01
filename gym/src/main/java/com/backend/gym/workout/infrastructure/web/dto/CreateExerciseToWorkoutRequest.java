package com.backend.gym.workout.infrastructure.web.dto;

import java.util.UUID;

public record CreateExerciseToWorkoutRequest(
    UUID workoutId,
    UUID exerciseId,
    Integer position,
    Integer targetSets,
    String targetReps,
    String notes
) {}