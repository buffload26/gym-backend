package com.backend.gym.competition.infrastructure.persistence;

import java.util.UUID;

public record CompetitionParticipantCountProjection(
    UUID competitionId,
    Long count
) {}