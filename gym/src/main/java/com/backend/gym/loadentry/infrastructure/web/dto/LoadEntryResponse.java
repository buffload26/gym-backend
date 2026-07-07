package com.backend.gym.loadentry.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.exercise.infrastructure.web.dto.ExerciseResponse;
import com.backend.gym.loadentry.domain.LoadEntry;
import com.backend.gym.user.infrastructure.web.dto.UserResponse;
import com.backend.gym.workout.infrastructure.web.dto.WorkoutResponse;

public record LoadEntryResponse(
    UUID id,
    UserResponse user,
    ExerciseResponse exercise,
    WorkoutResponse workout,
    LocalDate performedAt,
    BigDecimal loadKg,
    BigDecimal warmupLoadKg,
    Integer sets,
    Integer reps,
    String notes,
    LocalDateTime createdAt
) {
    public static LoadEntryResponse fromDomain(LoadEntry loadEntry) {
        return new LoadEntryResponse(
            loadEntry.id(),
            UserResponse.fromDomain(loadEntry.user()),
            ExerciseResponse.fromDomain(loadEntry.exercise()),
            loadEntry.workout() != null ? WorkoutResponse.fromDomain(loadEntry.workout()) : null,
            loadEntry.performedAt(),
            loadEntry.loadKg(),
            loadEntry.warmupLoadKg(),
            loadEntry.sets(),
            loadEntry.reps(),
            loadEntry.notes(),
            loadEntry.createdAt()
        );
    }
}
