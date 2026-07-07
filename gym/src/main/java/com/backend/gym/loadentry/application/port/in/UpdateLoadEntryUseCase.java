package com.backend.gym.loadentry.application.port.in;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.backend.gym.loadentry.domain.LoadEntry;

public interface UpdateLoadEntryUseCase {
    LoadEntry execute(UpdateLoadEntryCommand command);

    record UpdateLoadEntryCommand(
        UUID id,
        LocalDate performedAt,
        BigDecimal loadKg,
        BigDecimal warmupLoadKg,
        Integer sets,
        Integer reps,
        String notes
    ) {}
}