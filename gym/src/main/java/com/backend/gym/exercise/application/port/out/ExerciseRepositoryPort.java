package com.backend.gym.exercise.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.backend.gym.exercise.domain.Exercise;

public interface ExerciseRepositoryPort {
    Exercise save(Exercise exercise);
    Optional<Exercise> findById(UUID id);
    List<Exercise> findAll();
    List<Exercise> findAllByCreatedByUserId(UUID userId);
    List<Exercise> findAllActive();
}