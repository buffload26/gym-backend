package com.backend.gym.competition.application.port.in;

import java.util.UUID;

import com.backend.gym.competition.domain.Competition;

public interface FinishCompetitionUseCase {
    Competition finish(UUID id);
}