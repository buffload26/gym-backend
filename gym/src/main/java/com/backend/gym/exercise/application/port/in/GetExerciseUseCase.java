package com.backend.gym.exercise.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.exercise.domain.ExerciseWithLoads;

public interface GetExerciseUseCase {
    Exercise findById(UUID id);
    Page<Exercise> findAll(Pageable pageable);
    Page<Exercise> findAllByUser(UUID userId, Pageable pageable);
    Page<ExerciseWithLoads> findAllByUserWithLoads(UUID userId, Pageable pageable);
    Page<ExerciseWithLoads> findAllByFilter(ExerciseFilter filter, Pageable pageable);

    record ExerciseFilter(
        UUID userId,
        String muscleGroup
    ) {}
}
