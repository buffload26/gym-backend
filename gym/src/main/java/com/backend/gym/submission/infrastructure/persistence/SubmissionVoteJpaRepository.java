package com.backend.gym.submission.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.gym.submission.domain.SubmissionVoteType;

public interface SubmissionVoteJpaRepository extends JpaRepository<SubmissionVoteEntity, UUID> {

    boolean existsBySubmissionIdAndVoterId(UUID submissionId, UUID voterId);
    long countBySubmissionId(UUID submissionId);
    long countBySubmissionIdAndVote(UUID submissionId, SubmissionVoteType vote);
    Page<SubmissionVoteEntity> findAllBySubmissionId(UUID submissionId, Pageable pageable);
}