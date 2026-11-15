package com.backend.gym.competition.application.port.in;

import java.util.UUID;

import com.backend.gym.competition.domain.CompetitionParticipant;

public interface InviteCompetitionParticipantUseCase {
    record InviteCompetitionParticipantCommand(
        UUID competitionId,
        UUID userId
    ) {}

    CompetitionParticipant execute(InviteCompetitionParticipantCommand command);
}