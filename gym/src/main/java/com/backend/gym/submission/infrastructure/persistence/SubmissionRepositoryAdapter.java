package com.backend.gym.submission.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.backend.gym.submission.application.port.out.SubmissionRepositoryPort;
import com.backend.gym.submission.domain.Submission;
import com.backend.gym.submission.domain.SubmissionStatus;
import com.backend.gym.submission.infrastructure.persistence.specification.SubmissionSpecification;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubmissionRepositoryAdapter implements SubmissionRepositoryPort {

    private final SubmissionJpaRepository jpaRepository;

    @Override
    public Submission save(Submission submission) {
        return jpaRepository.save(
            SubmissionEntity.fromDomain(submission)
        ).toDomain();
    }

    @Override
    public Optional<Submission> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(SubmissionEntity::toDomain);
    }

    @Override
    public boolean existsByCompetitionIdAndUserId(UUID competitionId, UUID userId) {
        return jpaRepository.exists(
            Specification
                .where(SubmissionSpecification.byCompetitionId(competitionId))
                .and(SubmissionSpecification.byUserId(userId))
        );
    }

    @Override
    public boolean existsByCompetitionIdAndUserIdAndStatus(
            UUID competitionId, UUID userId, SubmissionStatus status) {
        return jpaRepository.exists(
            Specification
                .where(SubmissionSpecification.byCompetitionId(competitionId))
                .and(SubmissionSpecification.byUserId(userId))
                .and(SubmissionSpecification.byStatus(status))
        );
    }

    @Override
    public Page<Submission> findAllByCompetitionId(UUID competitionId, Pageable pageable) {
        Specification<SubmissionEntity> specification =
            Specification
                .where(SubmissionSpecification.byCompetitionId(competitionId));

        return jpaRepository.findAll(specification, pageable)
            .map(SubmissionEntity::toDomain);
    }

    @Override
    public Page<Submission> findAllByCompetitionIdAndStatus(
            UUID competitionId,
            SubmissionStatus status,
            Pageable pageable
    ) {
        Specification<SubmissionEntity> specification =
            Specification
                .where(SubmissionSpecification.byCompetitionId(competitionId))
                .and(SubmissionSpecification.byStatus(status));

        return jpaRepository.findAll(specification, pageable)
            .map(SubmissionEntity::toDomain);
    }

    @Override
    public Page<Submission> findAcceptedRankingByCompetitionId(UUID competitionId, Pageable pageable) {
        return jpaRepository.findAcceptedRankingByCompetitionId(competitionId, pageable)
            .map(SubmissionEntity::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}