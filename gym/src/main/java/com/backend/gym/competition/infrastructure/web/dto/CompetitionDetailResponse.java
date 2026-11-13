package com.backend.gym.competition.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.backend.gym.competition.domain.Competition;
import com.backend.gym.competition.domain.CompetitionStatus;
import com.backend.gym.competition.domain.CompetitionWithParticipants;
import com.backend.gym.exercise.infrastructure.web.dto.ExerciseResponse;
import com.backend.gym.user.infrastructure.web.dto.UserResponse;

public record CompetitionDetailResponse(
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
    List<UserResponse> participants
) {
    public static CompetitionDetailResponse fromDomain(
            CompetitionWithParticipants competitionWithParticipants) {

        Competition competition = competitionWithParticipants.competition();

        return new CompetitionDetailResponse(
            competition.id(),
            competition.title(),
            competition.description(),
            competition.rules(),
            competition.imageUrl(),
            ExerciseResponse.fromDomain(competition.exercise()),
            competition.createdByUser() != null
                ? UserResponse.fromDomain(competition.createdByUser())
                : null,
            competition.status(),
            competition.startAt(),
            competition.endAt(),
            competition.createdAt(),
            competition.updatedAt(),
            competitionWithParticipants.participants().stream()
                .map(UserResponse::fromDomain)
                .toList()
        );
    }
}