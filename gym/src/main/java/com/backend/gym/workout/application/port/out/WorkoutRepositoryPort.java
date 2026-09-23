package com.backend.gym.workout.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.workout.domain.Workout;

public interface WorkoutRepositoryPort {
    Workout save(Workout workout);
    Optional<Workout> findById(UUID id);
    Page<Workout> findAllByUserId(UUID userId, Pageable pageable);
    List<Workout> findAllByUserIdOrdered(UUID userId);
    void deleteById(UUID id);
}