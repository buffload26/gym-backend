package com.backend.gym.loadentry.application.port.in;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import com.backend.gym.loadentry.domain.LoadEntry;

public interface CreateLoadEntryUseCase {
    LoadEntry execute(CreateLoadEntryCommand command);

    record CreateLoadEntryCommand(
        UUID userId,
        UUID exerciseId,
        UUID workoutId,
        LocalDate performedAt,
        BigDecimal loadKg,
        Integer sets,
        Integer reps,
        String notes
    ) {}
}
