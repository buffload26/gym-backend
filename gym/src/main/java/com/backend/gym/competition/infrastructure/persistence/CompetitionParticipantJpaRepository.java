package com.backend.gym.competition.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

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
}