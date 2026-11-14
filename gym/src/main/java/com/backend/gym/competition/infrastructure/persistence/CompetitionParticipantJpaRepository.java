package com.backend.gym.competition.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompetitionParticipantJpaRepository extends
        JpaRepository<CompetitionParticipantEntity, UUID> {

    List<CompetitionParticipantEntity> findAllByCompetitionId(UUID competitionId);
    boolean existsByCompetitionIdAndUserId(
        UUID competitionId,
        UUID userId
    );
    void deleteByCompetitionIdAndUserId(
        UUID competitionId,
        UUID userId
    );
    @Query("""
        SELECT COUNT(cp.id)
        FROM CompetitionParticipantEntity cp
        WHERE cp.competition.id = :competitionId
    """)
    long countByCompetitionId(@Param("competitionId") UUID competitionId);

    @Query("""
        SELECT new com.backend.gym.competition.infrastructure.persistence.CompetitionParticipantCountProjection(
            cp.competition.id,
            COUNT(cp.id)
        )
        FROM CompetitionParticipantEntity cp
        WHERE cp.competition.id IN :competitionIds
        GROUP BY cp.competition.id
    """)
    List<CompetitionParticipantCountProjection> countByCompetitionIds(
        @Param("competitionIds") List<UUID> competitionIds
    );
}