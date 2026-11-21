package com.backend.gym.competition.infrastructure.persistence.projection;

import java.util.UUID;

public record CompetitionParticipantCountProjection(
    UUID competitionId,
    Long count
) {}