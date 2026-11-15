package com.backend.gym.competition.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.competition.domain.CompetitionParticipant;
import com.backend.gym.competition.domain.CompetitionParticipantStatus;
import com.backend.gym.user.infrastructure.web.dto.UserResponse;

public record CompetitionParticipantResponse(
    UUID id,
    UUID competitionId,
    UserResponse user,
    CompetitionParticipantStatus status,
    LocalDateTime joinedAt,
    LocalDateTime invitedAt
) {
    public static CompetitionParticipantResponse fromDomain(CompetitionParticipant participant) {
        return new CompetitionParticipantResponse(
            participant.id(),
            participant.competition() != null ? participant.competition().id() : null,
            participant.user() != null ? UserResponse.fromDomain(participant.user()) : null,
            participant.status(),
            participant.joinedAt(),
            participant.invitedAt()
        );
    }
}