package com.backend.gym.exercise.infrastructure.web.dto;

public record UpdateExerciseRequest(
    String name,
    String description,
    String muscleGroup,
    String imageUrl,
    String videoUrl
) {}
