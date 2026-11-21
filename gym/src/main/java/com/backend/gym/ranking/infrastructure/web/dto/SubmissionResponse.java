package com.backend.gym.ranking.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.competition.infrastructure.web.dto.CompetitionResponse;
import com.backend.gym.ranking.domain.Submission;
import com.backend.gym.ranking.domain.SubmissionStatus;
import com.backend.gym.user.infrastructure.web.dto.UserResponse;

public record SubmissionResponse(
    UUID id,
    CompetitionResponse competition,
    UserResponse user,
    String videoUrl,
    LocalDateTime videoDeletedAt,
    SubmissionStatus status,
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
            submission.createdAt(),
            submission.updatedAt()
        );
    }
}