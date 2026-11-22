package com.backend.gym.submission.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SubmissionJpaRepository extends
        JpaRepository<SubmissionEntity, UUID>,
        JpaSpecificationExecutor<SubmissionEntity> {
    
    @Query("""
        SELECT s FROM SubmissionEntity s
        WHERE s.competition.id = :competitionId
        AND s.status = 'ACCEPTED'
        AND s.createdAt = (
            SELECT MAX(s2.createdAt)
            FROM SubmissionEntity s2
            WHERE s2.user.id = s.user.id
                AND s2.competition.id = :competitionId
                AND s2.status = 'ACCEPTED'
        )
        ORDER BY s.loadKg DESC
    """)
    Page<SubmissionEntity> findAcceptedRankingByCompetitionId(
        @Param("competitionId") UUID competitionId,
        Pageable pageable
    );
    boolean existsByCompetitionIdAndUserId(UUID competitionId, UUID userId);
}