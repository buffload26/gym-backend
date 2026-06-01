package com.backend.gym.workout.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutExerciseJpaRepository extends JpaRepository<WorkoutExerciseEntity, UUID> {
    List<WorkoutExerciseEntity> findAllByWorkoutId(UUID workoutId);
}
