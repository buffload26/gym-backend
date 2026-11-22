package com.backend.gym.submission.application.port.out;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.submission.domain.SubmissionVote;
import com.backend.gym.submission.domain.SubmissionVoteType;

public interface SubmissionVoteRepositoryPort {

    SubmissionVote save(SubmissionVote vote);
    boolean existsBySubmissionIdAndVoterId(UUID submissionId, UUID voterId);
    long countBySubmissionId(UUID submissionId);
    long countBySubmissionIdAndVote(UUID submissionId, SubmissionVoteType vote);
    Page<SubmissionVote> findAllBySubmissionId(UUID submissionId, Pageable pageable);
}