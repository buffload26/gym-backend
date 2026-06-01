package com.backend.gym.workout.infrastructure.web.dto;

public record UpdateWorkoutExerciseRequest(
    Integer position,
    Integer targetSets,
    String targetReps,
    String notes
) {}
