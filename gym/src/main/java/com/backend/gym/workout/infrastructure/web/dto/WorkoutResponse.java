package com.backend.gym.workout.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.user.infrastructure.web.dto.UserResponse;
import com.backend.gym.workout.domain.Workout;

public record WorkoutResponse(
    UUID id,
    UserResponse user,
    String name,
    String description,
    String imageUrl,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static WorkoutResponse fromDomain(Workout workout) {
        return new WorkoutResponse(
            workout.id(), UserResponse.fromDomain(workout.user()), workout.name(),
            workout.description(), workout.imageUrl(), workout.createdAt(), workout.updatedAt()
        );
    }
}
