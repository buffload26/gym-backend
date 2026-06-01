package com.backend.gym.workout.application.port.in;

import java.util.List;
import java.util.UUID;

import com.backend.gym.workout.domain.WorkoutExercise;

public interface GetWorkoutExerciseUseCase {
    WorkoutExercise findById(UUID id);
    List<WorkoutExercise> findAllByWorkout(UUID workoutId);
}
