package com.backend.gym.competition.application.port.out;

import java.util.List;
import java.util.UUID;

import com.backend.gym.competition.domain.CompetitionParticipant;

public interface CompetitionParticipantRepositoryPort {

    CompetitionParticipant save(CompetitionParticipant participant);
    List<CompetitionParticipant> findAllByCompetitionId(UUID competitionId);
    boolean existsByCompetitionIdAndUserId(
        UUID competitionId,
        UUID userId
    );
    void deleteByCompetitionIdAndUserId(
        UUID competitionId,
        UUID userId
    );
}