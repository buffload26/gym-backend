package com.backend.gym.submission.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubmissionJpaRepository extends
        JpaRepository<SubmissionEntity, UUID>,
        JpaSpecificationExecutor<SubmissionEntity> {

    boolean existsByCompetitionIdAndUserId(UUID competitionId, UUID userId);
}