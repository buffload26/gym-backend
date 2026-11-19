package com.backend.gym.competition.infrastructure.persistence;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.backend.gym.competition.application.port.out.CompetitionParticipantRepositoryPort;
import com.backend.gym.competition.domain.CompetitionParticipant;
import com.backend.gym.competition.domain.CompetitionParticipantStatus;

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
    public Optional<CompetitionParticipant> findByCompetitionIdAndUserId(
            UUID competitionId, UUID userId) {
        return jpaRepository.findByCompetitionIdAndUserId(competitionId, userId)
            .map(CompetitionParticipantEntity::toDomain);
    }

    @Override
    public List<CompetitionParticipant> findAllByCompetitionId(UUID competitionId) {
        return jpaRepository.findAllByCompetitionId(competitionId)
            .stream()
            .map(CompetitionParticipantEntity::toDomain)
            .toList();
    }

    @Override
    public Page<CompetitionParticipant> findAllByFilter(
            UUID competitionId,
            UUID userId,
            CompetitionParticipantStatus status,
            Pageable pageable
    ) {
        Specification<CompetitionParticipantEntity> specification =
            Specification
                .where(CompetitionParticipantSpecification.byCompetitionId(competitionId))
                .and(CompetitionParticipantSpecification.byUserId(userId))
                .and(CompetitionParticipantSpecification.byStatus(status));

        return jpaRepository.findAll(specification, pageable)
            .map(CompetitionParticipantEntity::toDomain);
    }

    @Override
    public boolean existsByCompetitionIdAndUserId(UUID competitionId, UUID userId) {
        return jpaRepository.existsByCompetitionIdAndUserId(competitionId, userId);
    }

    @Override
    public void deleteByCompetitionIdAndUserId(UUID competitionId, UUID userId) {
        jpaRepository.deleteByCompetitionIdAndUserId(competitionId, userId);
    }

    @Override
    public long countByCompetitionId(UUID competitionId) {
        return jpaRepository.countByCompetitionId(competitionId);
    }

    @Override
    public Map<UUID, Long> countByCompetitionIds(List<UUID> competitionIds) {
        if (competitionIds.isEmpty()) {
            return Map.of();
        }

        return jpaRepository.countByCompetitionIds(competitionIds)
            .stream()
            .collect(Collectors.toMap(
                CompetitionParticipantCountProjection::competitionId,
                CompetitionParticipantCountProjection::count
            ));
    }
}