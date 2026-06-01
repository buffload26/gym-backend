package com.backend.gym.shared.exception.exercise;

import java.util.UUID;

import com.backend.gym.shared.exception.domain.NotFoundException;

public class ExerciseNotFoundException extends NotFoundException {
    public ExerciseNotFoundException(UUID id) {
        super("Exercise not found with id: " + id);
    }
}
