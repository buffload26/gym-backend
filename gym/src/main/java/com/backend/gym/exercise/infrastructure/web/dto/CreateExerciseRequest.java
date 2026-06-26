package com.backend.gym.exercise.infrastructure.web.dto;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public record CreateExerciseRequest(
    String name,
    String description,
    String muscleGroup,
    MultipartFile image,
    String videoUrl,
    boolean isDefault,
    UUID createdByUserId
) {}