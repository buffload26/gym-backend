package com.backend.gym.competition.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public record CreateCompetitionRequest(
    String title,
    String description,
    String rules,
    MultipartFile image,
    UUID exerciseId,
    UUID createdByUserId,
    LocalDateTime startAt,
    LocalDateTime endAt
) {}