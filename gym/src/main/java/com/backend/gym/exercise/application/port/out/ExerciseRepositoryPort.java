package com.backend.gym.exercise.application.port.out;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.exercise.domain.Exercise;

public interface ExerciseRepositoryPort {
    Exercise save(Exercise exercise);
    Optional<Exercise> findById(UUID id);
    Page<Exercise> findAllActive(Pageable pageable);
    Page<Exercise> findAllByCreatedByUserIdorIsDefault(UUID userId, Pageable pageable);
}