package com.backend.gym.exercise.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.backend.gym.exercise.application.port.out.ExerciseRepositoryPort;
import com.backend.gym.exercise.domain.Exercise;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExerciseRepositoryAdapter implements ExerciseRepositoryPort {

    private final ExerciseJpaRepository jpaRepository;

    @Override
    public Exercise save(Exercise exercise) {
        return jpaRepository.save(ExerciseEntity.fromDomain(exercise)).toDomain();
    }

    @Override
    public Optional<Exercise> findById(UUID id) {
        return jpaRepository.findById(id).map(ExerciseEntity::toDomain);
    }

    @Override
    public List<Exercise> findFavoritesByUserId(UUID userId) {
        return jpaRepository.findFavoritesByUserId(userId).stream()
            .map(ExerciseEntity::toDomain)
            .toList();
    }

    @Override
    public Page<Exercise> findAllActive(Pageable pageable) {
        return jpaRepository.findAllByDeletedAtIsNull(pageable)
            .map(ExerciseEntity::toDomain);
    }

    @Override
    public Page<Exercise> findAllByCreatedByUserIdorIsDefault(UUID userId, Pageable pageable) {
        return jpaRepository.findAllByCreatedByUserIdOrIsDefaultAndDeletedAtIsNull(userId, pageable)
            .map(ExerciseEntity::toDomain);
    }

    @Override
    public Page<Exercise> findAllByFilter(UUID userId, String muscleGroup, Pageable pageable) {
        Specification<ExerciseEntity> spec = Specification
            .where(ExerciseSpecification.isNotDeleted())
            .and(ExerciseSpecification.byUserId(userId))
            .and(ExerciseSpecification.byMuscleGroup(muscleGroup));

        return jpaRepository.findAll(spec, pageable)
            .map(ExerciseEntity::toDomain);
    }
}
