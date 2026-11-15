package com.backend.gym.competition.application.port.in;

import java.util.UUID;

import com.backend.gym.competition.domain.CompetitionParticipant;

public interface AcceptCompetitionInviteUseCase {
    CompetitionParticipant accept(UUID competitionId, UUID userId);
}
