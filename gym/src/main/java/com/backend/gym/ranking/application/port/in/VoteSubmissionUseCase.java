package com.backend.gym.ranking.application.port.in;

import java.util.UUID;

import com.backend.gym.ranking.domain.SubmissionVote;
import com.backend.gym.ranking.domain.SubmissionVoteType;

public interface VoteSubmissionUseCase {

    SubmissionVote execute(VoteSubmissionCommand command);

    record VoteSubmissionCommand(
        UUID submissionId,
        UUID voterUserId,
        SubmissionVoteType vote
    ) {}
}