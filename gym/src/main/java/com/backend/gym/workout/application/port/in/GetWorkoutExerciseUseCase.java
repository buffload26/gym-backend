package com.backend.gym.workout.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.workout.domain.WorkoutExercise;

public interface GetWorkoutExerciseUseCase {
    WorkoutExercise findById(UUID id);
    Page<WorkoutExercise> findAllByWorkout(UUID workoutId, Pageable pageable);
}
