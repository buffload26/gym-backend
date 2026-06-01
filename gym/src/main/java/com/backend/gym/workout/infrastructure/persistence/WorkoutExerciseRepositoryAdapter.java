package com.backend.gym.workout.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public List<WorkoutExercise> findAllByWorkoutId(UUID workoutId) {
        return jpaRepository.findAllByWorkoutId(workoutId).stream()
            .map(WorkoutExerciseEntity::toDomain)
            .toList();
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
