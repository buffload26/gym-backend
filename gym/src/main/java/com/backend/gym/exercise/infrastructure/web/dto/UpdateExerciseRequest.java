package com.backend.gym.exercise.infrastructure.web.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdateExerciseRequest(
    String name,
    String description,
    String muscleGroup,
    MultipartFile image,
    String videoUrl,
    Boolean isFavorite
) {}
