package com.backend.gym.workout.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.backend.gym.workout.domain.WorkoutExercise;

public interface WorkoutExerciseRepositoryPort {
    WorkoutExercise save(WorkoutExercise workoutExercise);
    Optional<WorkoutExercise> findById(UUID id);
    List<WorkoutExercise> findAllByWorkoutId(UUID workoutId);
    void deleteById(UUID id);
}
