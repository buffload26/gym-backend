package com.backend.gym.submission.infrastructure.web.dto;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public record CreateSubmissionRequest(
    UUID competitionId,
    UUID userId,
    MultipartFile video,
    BigDecimal loadKg,
    Integer repetitions,
    String observation
) {}