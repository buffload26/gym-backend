package com.backend.gym.competition.application.port.out;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.backend.gym.competition.domain.CompetitionParticipant;

public interface CompetitionParticipantRepositoryPort {
    CompetitionParticipant save(CompetitionParticipant participant);
    boolean existsByCompetitionIdAndUserId(UUID competitionId, UUID userId);
    List<CompetitionParticipant> findAllByCompetitionId(UUID competitionId);
    Optional<CompetitionParticipant> findByCompetitionIdAndUserId(UUID competitionId, UUID userId);
    void deleteByCompetitionIdAndUserId(UUID competitionId, UUID userId);
    long countByCompetitionId(UUID competitionId);
    Map<UUID, Long> countByCompetitionIds(List<UUID> competitionIds);
}