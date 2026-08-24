package com.backend.gym.workout.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.backend.gym.workout.application.port.out.WorkoutExerciseRepositoryPort;
import com.backend.gym.workout.domain.WorkoutExercise;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class WorkoutExerciseRepositoryAdapter implements WorkoutExerciseRepositoryPort {

    private final WorkoutExerciseJpaRepository jpaRepository;

    @Override
    public WorkoutExercise save(WorkoutExercise workoutExercise) {
        return jpaRepository.save(WorkoutExerciseEntity.fromDomain(workoutExercise)).toDomain();
    }

    @Override
    public Optional<WorkoutExercise> findById(UUID id) {
        return jpaRepository.findById(id).map(WorkoutExerciseEntity::toDomain);
    }

    @Override
    public List<WorkoutExercise> findAllByWorkoutIdOrdered(UUID workoutId) {
        return jpaRepository.findAllByWorkoutIdOrderBySortOrderAsc(workoutId).stream()
            .map(WorkoutExerciseEntity::toDomain)
            .toList();
    }

    @Override
    public Page<WorkoutExercise> findAllByWorkoutId(UUID workoutId, Pageable pageable) {
        return jpaRepository.findAllByWorkoutId(workoutId, pageable)
            .map(WorkoutExerciseEntity::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
