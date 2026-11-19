package com.backend.gym.competition.application.port.out;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.competition.domain.CompetitionParticipant;
import com.backend.gym.competition.domain.CompetitionParticipantStatus;

public interface CompetitionParticipantRepositoryPort {
    CompetitionParticipant save(CompetitionParticipant participant);
    boolean existsByCompetitionIdAndUserId(UUID competitionId, UUID userId);
    Optional<CompetitionParticipant> findByCompetitionIdAndUserId(UUID competitionId, UUID userId);
    List<CompetitionParticipant> findAllByCompetitionId(UUID competitionId);
    Page<CompetitionParticipant> findAllByFilter(
        UUID competitionId,
        UUID userId,
        CompetitionParticipantStatus status,
        Pageable pageable
    );
    void deleteByCompetitionIdAndUserId(UUID competitionId, UUID userId);
    long countByCompetitionId(UUID competitionId);
    Map<UUID, Long> countByCompetitionIds(List<UUID> competitionIds);
}