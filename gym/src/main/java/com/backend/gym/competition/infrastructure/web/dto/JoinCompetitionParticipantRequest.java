package com.backend.gym.competition.infrastructure.web.dto;

import java.util.UUID;

public record JoinCompetitionParticipantRequest(
    UUID userId
) {}