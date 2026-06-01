package com.backend.gym.exercise.application.port.in;

import java.util.UUID;

import com.backend.gym.exercise.domain.Exercise;

public interface UpdateExerciseUseCase {
    Exercise execute(UpdateExerciseCommand command);

    record UpdateExerciseCommand(
        UUID id,
        String name,
        String description,
        String muscleGroup,
        String imageUrl,
        String videoUrl
    ) {}
}