package com.backend.gym.competition.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.competition.domain.Competition;
import com.backend.gym.competition.domain.CompetitionStatus;
import com.backend.gym.exercise.infrastructure.web.dto.ExerciseResponse;
import com.backend.gym.user.infrastructure.web.dto.UserResponse;

public record CompetitionResponse(
    UUID id,
    String title,
    String description,
    String rules,
    String imageUrl,
    ExerciseResponse exercise,
    UserResponse createdByUser,
    CompetitionStatus status,
    LocalDateTime startAt,
    LocalDateTime endAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    long participantCount
) {
    public static CompetitionResponse fromDomain(Competition competition) {
        return fromDomain(competition, 0L);
    }

    public static CompetitionResponse fromDomain(Competition competition, long participantCount) {
        return new CompetitionResponse(
            competition.id(),
            competition.title(),
            competition.description(),
            competition.rules(),
            competition.imageUrl(),
            competition.exercise() != null
                ? ExerciseResponse.fromDomain(competition.exercise())
                : null,
            competition.createdByUser() != null
                ? UserResponse.fromDomain(competition.createdByUser())
                : null,
            competition.status(),
            competition.startAt(),
            competition.endAt(),
            competition.createdAt(),
            competition.updatedAt(),
            participantCount
        );
    }
}