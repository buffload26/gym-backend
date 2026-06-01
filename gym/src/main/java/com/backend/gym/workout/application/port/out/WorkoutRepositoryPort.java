package com.backend.gym.workout.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.backend.gym.workout.domain.Workout;

public interface WorkoutRepositoryPort {
    Workout save(Workout workout);
    Optional<Workout> findById(UUID id);
    List<Workout> findAllByUserId(UUID userId);
    void deleteById(UUID id);
}