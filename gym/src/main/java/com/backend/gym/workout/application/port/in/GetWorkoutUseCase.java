package com.backend.gym.workout.application.port.in;

import java.util.List;
import java.util.UUID;

import com.backend.gym.workout.domain.Workout;

public interface GetWorkoutUseCase {
    Workout findById(UUID id);
    List<Workout> findAllByUser(UUID userId);
}
