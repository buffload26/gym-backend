package com.backend.gym.competition.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.competition.domain.CompetitionParticipant;
import com.backend.gym.user.infrastructure.web.dto.UserResponse;

public record CompetitionParticipantResponse(
    UUID id,
    UserResponse user,
    CompetitionResponse competition,
    LocalDateTime joinedAt
) {

    public static CompetitionParticipantResponse fromDomain(
            CompetitionParticipant participant) {

        return new CompetitionParticipantResponse(
            participant.id(),
            UserResponse.fromDomain(participant.user()),
            CompetitionResponse.fromDomain(participant.competition()),
            participant.joinedAt()
        );
    }
}