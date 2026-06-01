package com.backend.gym.workout.domain;

import java.util.UUID;

import com.backend.gym.exercise.domain.Exercise;

public record WorkoutExercise(
    UUID id,
    Workout workout,
    Exercise exercise,
    Integer position,
    Integer targetSets,
    String targetReps,
    String notes
) {}
