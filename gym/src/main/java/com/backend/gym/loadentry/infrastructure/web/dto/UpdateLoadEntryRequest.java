package com.backend.gym.loadentry.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateLoadEntryRequest(
    LocalDate performedAt,
    BigDecimal loadKg,
    BigDecimal warmupLoadKg,
    Integer sets,
    Integer reps,
    String notes
) {}