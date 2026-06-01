package com.backend.gym.workout.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.backend.gym.workout.application.port.out.WorkoutRepositoryPort;
import com.backend.gym.workout.domain.Workout;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WorkoutRepositoryAdapter implements WorkoutRepositoryPort {

    private final WorkoutJpaRepository jpaRepository;

    @Override
    public Workout save(Workout workout) {
        return jpaRepository.save(WorkoutEntity.fromDomain(workout)).toDomain();
    }

    @Override
    public Optional<Workout> findById(UUID id) {
        return jpaRepository.findById(id).map(WorkoutEntity::toDomain);
    }

    @Override
    public Page<Workout> findAllByUserId(UUID userId, Pageable pageable) {
        return jpaRepository.findAllByUserId(userId, pageable)
            .map(WorkoutEntity::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
