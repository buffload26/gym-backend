package com.backend.gym.submission.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.backend.gym.submission.application.port.out.SubmissionVoteRepositoryPort;
import com.backend.gym.submission.domain.SubmissionVote;
import com.backend.gym.submission.domain.SubmissionVoteType;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubmissionVoteRepositoryAdapter implements SubmissionVoteRepositoryPort {

    private final SubmissionVoteJpaRepository jpaRepository;

    @Override
    public SubmissionVote save(SubmissionVote vote) {
        return jpaRepository.save(
            SubmissionVoteEntity.fromDomain(vote)
        ).toDomain();
    }

    @Override
    public boolean existsBySubmissionIdAndVoterId(UUID submissionId, UUID voterId) {
        return jpaRepository.existsBySubmissionIdAndVoterId(submissionId, voterId);
    }

    @Override
    public long countBySubmissionId(UUID submissionId) {
        return jpaRepository.countBySubmissionId(submissionId);
    }

    @Override
    public long countBySubmissionIdAndVote(UUID submissionId, SubmissionVoteType vote) {
        return jpaRepository.countBySubmissionIdAndVote(submissionId, vote);
    }

    @Override
    public Page<SubmissionVote> findAllBySubmissionId(UUID submissionId, Pageable pageable) {
        return jpaRepository.findAllBySubmissionId(submissionId, pageable)
            .map(SubmissionVoteEntity::toDomain);
    }
}