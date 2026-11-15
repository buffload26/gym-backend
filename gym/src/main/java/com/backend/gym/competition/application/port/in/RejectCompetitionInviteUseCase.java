package com.backend.gym.competition.application.port.in;

import java.util.UUID;

import com.backend.gym.competition.domain.CompetitionParticipant;

public interface RejectCompetitionInviteUseCase {
    CompetitionParticipant reject(UUID competitionId, UUID userId);
}
