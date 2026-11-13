package com.backend.gym.competition.application.port.in;

import java.util.UUID;

import com.backend.gym.competition.domain.CompetitionParticipant;

public interface JoinCompetitionParticipantUseCase {

    CompetitionParticipant execute(JoinCompetitionParticipantCommand command);

    public record JoinCompetitionParticipantCommand(
        UUID competitionId,
        UUID userId
    ) {}
}