package com.backend.gym.competition.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.user.domain.User;

public record CompetitionParticipant(
    UUID id,
    Competition competition,
    User user,
    CompetitionParticipantStatus status,
    LocalDateTime joinedAt,
    LocalDateTime invitedAt
) {}