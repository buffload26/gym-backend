package com.backend.gym.submission.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.user.domain.User;

public record SubmissionVote(
    UUID id,
    Submission submission,
    User voter,
    SubmissionVoteType vote,
    LocalDateTime createdAt
) {}