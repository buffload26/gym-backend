
package com.backend.gym.competition.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.gym.competition.domain.CompetitionStatus;

public interface CompetitionJpaRepository extends
        JpaRepository<CompetitionEntity, UUID>,
        JpaSpecificationExecutor<CompetitionEntity> {

    Page<CompetitionEntity> findAllByDeletedAtIsNull(Pageable pageable);

    @Query("""
        SELECT c
        FROM CompetitionEntity c
        WHERE (:userId IS NULL OR c.createdByUser.id = :userId)
          AND (:exerciseId IS NULL OR c.exercise.id = :exerciseId)
          AND (:status IS NULL OR c.status = :status)
          AND c.deletedAt IS NULL
        """)
    Page<CompetitionEntity> findAllByFilter(
        @Param("userId") UUID userId,
        @Param("exerciseId") UUID exerciseId,
        @Param("status") CompetitionStatus status,
        Pageable pageable
    );
}
