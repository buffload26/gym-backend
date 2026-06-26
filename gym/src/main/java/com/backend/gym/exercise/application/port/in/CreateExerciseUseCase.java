package com.backend.gym.exercise.application.port.in;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.backend.gym.exercise.domain.Exercise;

public interface CreateExerciseUseCase {
    Exercise execute(CreateExerciseCommand command);

    record CreateExerciseCommand(
        String name,
        String description,
        String muscleGroup,
        MultipartFile image,
        String videoUrl,
        boolean isDefault,
        UUID createdByUserId
    ) {}
}
