package com.backend.gym.competition.application.port.in;

import java.util.UUID;

public interface DeleteCompetitionUseCase {
    void execute(UUID id);
}