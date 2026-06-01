package com.backend.gym.workout.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.workout.domain.Workout;

public interface GetWorkoutUseCase {
    Workout findById(UUID id);
    Page<Workout> findAllByUser(UUID userId, Pageable pageable);
}
