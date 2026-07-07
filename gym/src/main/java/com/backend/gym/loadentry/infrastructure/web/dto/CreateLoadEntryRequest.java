package com.backend.gym.loadentry.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CreateLoadEntryRequest(
    UUID userId,
    UUID exerciseId,
    UUID workoutId,
    LocalDate performedAt,
    BigDecimal loadKg,
    Integer sets,
    Integer reps,
    String notes
) {}