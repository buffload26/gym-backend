package com.backend.gym.exercise.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.exercise.domain.Exercise;

public interface GetExerciseUseCase {
    Exercise findById(UUID id);
    Page<Exercise> findAll(Pageable pageable);
    Page<Exercise> findAllByUser(UUID userId, Pageable pageable);
}
