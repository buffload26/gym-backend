package com.backend.gym.shared.exception.exercise;

import java.util.UUID;

import com.backend.gym.shared.exception.domain.NotFoundException;

public class WorkoutExerciseNotFoundException extends NotFoundException {
    public WorkoutExerciseNotFoundException(UUID id) {
        super("Workout exercise not found with id: " + id);
    }
}
