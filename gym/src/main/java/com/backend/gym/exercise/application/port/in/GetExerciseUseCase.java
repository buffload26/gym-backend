package com.backend.gym.exercise.application.port.in;

import java.util.List;
import java.util.UUID;

import com.backend.gym.exercise.domain.Exercise;

public interface GetExerciseUseCase {
    Exercise findById(UUID id);
    List<Exercise> findAll();
    List<Exercise> findAllByUser(UUID userId);
}
