package com.backend.gym.workout.application.port.out;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.workout.domain.WorkoutExercise;

public interface WorkoutExerciseRepositoryPort {
    WorkoutExercise save(WorkoutExercise workoutExercise);
    Optional<WorkoutExercise> findById(UUID id);
    Page<WorkoutExercise> findAllByWorkoutId(UUID workoutId, Pageable pageable);
    void deleteById(UUID id);
}
