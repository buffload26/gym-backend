package com.backend.gym.workout.infrastructure.web.dto;

import java.util.UUID;

public record CreateWorkoutRequest(UUID userId, String name, String description, String imageUrl) {}

