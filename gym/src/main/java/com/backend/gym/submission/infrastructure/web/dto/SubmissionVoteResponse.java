package com.backend.gym.submission.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.submission.domain.Submission;
import com.backend.gym.submission.domain.SubmissionVote;
import com.backend.gym.submission.domain.SubmissionVoteType;
import com.backend.gym.user.infrastructure.web.dto.UserResponse;

public record SubmissionVoteResponse(
    UUID id,
    Submission submission,
    UserResponse voter,
    SubmissionVoteType vote,
    LocalDateTime createdAt
) {
    public static SubmissionVoteResponse fromDomain(SubmissionVote vote) {
        return new SubmissionVoteResponse(
            vote.id(),
            vote.submission(),
            UserResponse.fromDomain(vote.voter()),
            vote.vote(),
            vote.createdAt()
        );
    }
}