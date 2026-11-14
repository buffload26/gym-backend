package com.backend.gym.competition.domain;

public record CompetitionWithParticipantCount(
    Competition competition,
    long participantCount
) {}