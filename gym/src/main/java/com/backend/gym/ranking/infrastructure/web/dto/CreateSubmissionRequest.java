package com.backend.gym.ranking.infrastructure.web.dto;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public record CreateSubmissionRequest(
    UUID competitionId,
    UUID userId,
    MultipartFile video
) {}