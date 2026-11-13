package com.backend.gym.competition.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.user.domain.User;

public record Competition(
    UUID id,
    String title,
    String description,
    String rules,
    String imageUrl,
    Exercise exercise,
    User createdByUser,
    CompetitionStatus status,
    LocalDateTime startAt,
    LocalDateTime endAt,
    LocalDateTime deletedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}