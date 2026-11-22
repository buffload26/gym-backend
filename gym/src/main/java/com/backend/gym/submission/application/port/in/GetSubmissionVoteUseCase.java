package com.backend.gym.submission.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.submission.domain.SubmissionVote;

public interface GetSubmissionVoteUseCase {

    Page<SubmissionVote> findAllBySubmission(UUID submissionId, Pageable pageable);
}