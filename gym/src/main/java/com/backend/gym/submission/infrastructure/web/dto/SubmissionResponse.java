package com.backend.gym.submission.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.competition.infrastructure.web.dto.CompetitionResponse;
import com.backend.gym.submission.domain.Submission;
import com.backend.gym.submission.domain.SubmissionStatus;
import com.backend.gym.user.infrastructure.web.dto.UserResponse;

public record SubmissionResponse(
    UUID id,
    CompetitionResponse competition,
    UserResponse user,
    String videoUrl,
    LocalDateTime videoDeletedAt,
    SubmissionStatus status,
    BigDecimal loadKg,
    Integer repetitions,
    String observation,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static SubmissionResponse fromDomain(Submission submission) {
        return new SubmissionResponse(
            submission.id(),
            CompetitionResponse.fromDomain(submission.competition()),
            UserResponse.fromDomain(submission.user()),
            submission.videoUrl(),
            submission.videoDeletedAt(),
            submission.status(),
            submission.loadKg(),
            submission.repetitions(),
            submission.observation(),
            submission.createdAt(),
            submission.updatedAt()
        );
    }
}