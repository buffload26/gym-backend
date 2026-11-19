package com.backend.gym.competition.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CompetitionParticipantJpaRepository extends
        JpaRepository<CompetitionParticipantEntity, UUID>,
        JpaSpecificationExecutor<CompetitionParticipantEntity> {
    
    @Query("""
        SELECT cp FROM CompetitionParticipantEntity cp
        WHERE cp.competition.id = :competitionId
        AND cp.user.id = :userId
    """)
    Optional<CompetitionParticipantEntity> findByCompetitionIdAndUserId(
        @Param("competitionId") UUID competitionId,
        @Param("userId") UUID userId
    );
   @Query("""
        SELECT cp
        FROM CompetitionParticipantEntity cp
        WHERE cp.competition.id = :competitionId
        AND cp.status = 'ACCEPTED'
    """)
    List<CompetitionParticipantEntity> findAllByCompetitionId(
        @Param("competitionId") UUID competitionId
    );
    @Query("""
        SELECT new com.backend.gym.competition.infrastructure.persistence.CompetitionParticipantCountProjection(
            cp.competition.id,
            COUNT(cp.id)
        )
        FROM CompetitionParticipantEntity cp
        WHERE cp.competition.id IN :competitionIds
        AND cp.status = 'ACCEPTED'
        GROUP BY cp.competition.id
    """)
    List<CompetitionParticipantCountProjection> countByCompetitionIds(
        @Param("competitionIds") List<UUID> competitionIds
    );
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
        AND cp.status = 'ACCEPTED'
    """)
    long countByCompetitionId(@Param("competitionId") UUID competitionId);
}