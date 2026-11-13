package com.backend.gym.competition.domain;

import java.util.List;

import com.backend.gym.user.domain.User;

public record CompetitionWithParticipants(
    Competition competition,
    List<User> participants
) {}