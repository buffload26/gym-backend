package com.backend.gym.exercise.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public List<Exercise> findAll() {
        return jpaRepository.findAll().stream()
            .map(ExerciseEntity::toDomain)
            .toList();
    }

    @Override
    public List<Exercise> findAllByCreatedByUserId(UUID userId) {
        return jpaRepository.findAllByCreatedByUserId(userId).stream()
            .map(ExerciseEntity::toDomain)
            .toList();
    }

    @Override
    public List<Exercise> findAllActive() {
        return jpaRepository.findAllByDeletedAtIsNull().stream()
            .map(ExerciseEntity::toDomain)
            .toList();
    }
}
