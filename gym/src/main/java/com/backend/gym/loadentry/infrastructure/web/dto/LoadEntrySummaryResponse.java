package com.backend.gym.loadentry.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.loadentry.domain.LoadEntry;

public record LoadEntrySimpleResponse(
    UUID id,
    LocalDate performedAt,
    BigDecimal loadKg,
    Integer sets,
    Integer reps,
    String notes,
    LocalDateTime createdAt
) {
    public static LoadEntrySimpleResponse fromDomain(LoadEntry loadEntry) {
        return new LoadEntrySimpleResponse(
            loadEntry.id(),
            loadEntry.performedAt(),
            loadEntry.loadKg(),
            loadEntry.sets(),
            loadEntry.reps(),
            loadEntry.notes(),
            loadEntry.createdAt()
        );
    }
}
