package com.backend.gym.ranking.application.port.out;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.ranking.domain.SubmissionVote;
import com.backend.gym.ranking.domain.SubmissionVoteType;

public interface SubmissionVoteRepositoryPort {

    SubmissionVote save(SubmissionVote vote);
    boolean existsBySubmissionIdAndVoterId(UUID submissionId, UUID voterId);
    long countBySubmissionId(UUID submissionId);
    long countBySubmissionIdAndVote(UUID submissionId, SubmissionVoteType vote);
    Page<SubmissionVote> findAllBySubmissionId(UUID submissionId, Pageable pageable);
}