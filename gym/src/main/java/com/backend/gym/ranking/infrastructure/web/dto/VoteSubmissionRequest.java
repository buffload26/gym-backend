package com.backend.gym.ranking.infrastructure.web.dto;

import java.util.UUID;

import com.backend.gym.ranking.domain.SubmissionVoteType;

public record VoteSubmissionRequest(
    UUID voterUserId,
    SubmissionVoteType vote
) {}