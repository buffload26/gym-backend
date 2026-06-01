package com.backend.gym.shared.exception.workout;

import java.util.UUID;

import com.backend.gym.shared.exception.domain.NotFoundException;

public class WorkoutNotFoundException extends NotFoundException {
    public WorkoutNotFoundException(UUID id) {
        super("Workout not found with id: " + id);
    }
}
