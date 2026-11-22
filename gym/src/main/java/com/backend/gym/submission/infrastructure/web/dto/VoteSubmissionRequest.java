package com.backend.gym.submission.infrastructure.web.dto;

import java.util.UUID;

import com.backend.gym.submission.domain.SubmissionVoteType;

public record VoteSubmissionRequest(
    UUID voterUserId,
    SubmissionVoteType vote
) {}