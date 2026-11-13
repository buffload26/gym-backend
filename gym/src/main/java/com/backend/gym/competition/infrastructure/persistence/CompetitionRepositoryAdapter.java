
package com.backend.gym.competition.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.backend.gym.competition.application.port.out.CompetitionRepositoryPort;
import com.backend.gym.competition.domain.Competition;
import com.backend.gym.competition.domain.CompetitionStatus;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompetitionRepositoryAdapter implements CompetitionRepositoryPort {

    private final CompetitionJpaRepository jpaRepository;

    @Override
    public Competition save(Competition competition) {
        return jpaRepository.save(CompetitionEntity.fromDomain(competition)).toDomain();
    }

    @Override
    public Optional<Competition> findById(UUID id) {
        return jpaRepository.findById(id).map(CompetitionEntity::toDomain);
    }

    @Override
    public Page<Competition> findAllActive(Pageable pageable) {
        return jpaRepository.findAllByDeletedAtIsNull(pageable)
            .map(CompetitionEntity::toDomain);
    }

    @Override
    public Page<Competition> findAllByFilter(
            UUID userId,
            UUID exerciseId,
            CompetitionStatus status,
            Pageable pageable) {

        Specification<CompetitionEntity> spec = Specification
            .where(CompetitionSpecification.isNotDeleted())
            .and(CompetitionSpecification.byUserId(userId))
            .and(CompetitionSpecification.byExerciseId(exerciseId))
            .and(CompetitionSpecification.byStatus(status));

        return jpaRepository.findAll(spec, pageable)
            .map(CompetitionEntity::toDomain);
    }
}
