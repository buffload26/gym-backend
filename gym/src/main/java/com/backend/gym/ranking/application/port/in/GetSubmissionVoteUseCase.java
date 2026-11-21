package com.backend.gym.ranking.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.ranking.domain.SubmissionVote;

public interface GetSubmissionVoteUseCase {

    Page<SubmissionVote> findAllBySubmission(UUID submissionId, Pageable pageable);
}