package com.backend.gym.loadentry.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.loadentry.domain.LoadEntry;

public record LoadEntrySummaryResponse(
    UUID id,
    LocalDate performedAt,
    BigDecimal loadKg,
    BigDecimal warmupLoadKg,
    Integer sets,
    Integer reps,
    String notes,
    LocalDateTime createdAt
) {
    public static LoadEntrySummaryResponse fromDomain(LoadEntry loadEntry) {
        return new LoadEntrySummaryResponse(
            loadEntry.id(),
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
