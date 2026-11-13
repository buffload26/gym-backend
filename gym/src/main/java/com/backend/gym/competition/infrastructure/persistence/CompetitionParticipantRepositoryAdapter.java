package com.backend.gym.competition.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.backend.gym.competition.application.port.out.CompetitionParticipantRepositoryPort;
import com.backend.gym.competition.domain.CompetitionParticipant;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompetitionParticipantRepositoryAdapter
        implements CompetitionParticipantRepositoryPort {

    private final CompetitionParticipantJpaRepository jpaRepository;

    @Override
    public CompetitionParticipant save(CompetitionParticipant participant) {
        return jpaRepository.save(
            CompetitionParticipantEntity.fromDomain(participant)
        ).toDomain();
    }

    @Override
    public List<CompetitionParticipant> findAllByCompetitionId(UUID competitionId) {
        return jpaRepository.findAllByCompetitionId(competitionId)
            .stream()
            .map(CompetitionParticipantEntity::toDomain)
            .toList();
    }

    @Override
    public boolean existsByCompetitionIdAndUserId(UUID competitionId, UUID userId) {
        return jpaRepository.existsByCompetitionIdAndUserId(competitionId, userId);
    }

    @Override
    public void deleteByCompetitionIdAndUserId(UUID competitionId, UUID userId) {
        jpaRepository.deleteByCompetitionIdAndUserId(competitionId, userId);
    }
}