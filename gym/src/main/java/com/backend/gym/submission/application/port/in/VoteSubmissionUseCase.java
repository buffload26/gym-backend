package com.backend.gym.submission.application.port.in;

import java.util.UUID;

import com.backend.gym.submission.domain.SubmissionVote;
import com.backend.gym.submission.domain.SubmissionVoteType;

public interface VoteSubmissionUseCase {

    SubmissionVote execute(VoteSubmissionCommand command);

    record VoteSubmissionCommand(
        UUID submissionId,
        UUID voterUserId,
        SubmissionVoteType vote
    ) {}
}