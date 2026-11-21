package com.backend.gym.ranking.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.competition.domain.Competition;
import com.backend.gym.user.domain.User;

public record Submission(
    UUID id,
    Competition competition,
    User user,
    String videoUrl,
    LocalDateTime videoDeletedAt,
    SubmissionStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}