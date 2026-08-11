package com.backend.gym.workout.domain;

import java.util.UUID;

import com.backend.gym.exercise.domain.ExerciseWithLoads;

public record WorkoutExercise(
    UUID id,
    Workout workout,
    ExerciseWithLoads exercise,
    Integer position,
    Integer targetSets,
    String targetReps,
    String notes,
    Integer sortOrder
) {}
