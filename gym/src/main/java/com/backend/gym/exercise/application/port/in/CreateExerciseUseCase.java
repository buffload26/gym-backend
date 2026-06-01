package com.backend.gym.exercise.application.port.in;

import java.util.UUID;

import com.backend.gym.exercise.domain.Exercise;

public interface CreateExerciseUseCase {
    Exercise execute(CreateExerciseCommand command);

    record CreateExerciseCommand(
        String name,
        String description,
        String muscleGroup,
        String imageUrl,
        String videoUrl,
        boolean isDefault,
        UUID createdByUserId
    ) {}
}
