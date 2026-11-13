package com.backend.gym.competition.application.port.in;

import java.util.UUID;

public interface GetCompetitionParticipantUseCase {

    boolean existsByCompetitionAndUser(
        UUID competitionId,
        UUID userId
    );
}