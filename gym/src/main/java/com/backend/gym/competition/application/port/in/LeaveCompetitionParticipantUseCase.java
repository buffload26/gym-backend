package com.backend.gym.competition.application.port.in;

import java.util.UUID;

public interface LeaveCompetitionParticipantUseCase {

    void execute(UUID competitionId, UUID userId);
}