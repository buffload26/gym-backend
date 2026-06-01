package com.backend.gym.workout.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.gym.exercise.application.port.out.ExerciseRepositoryPort;
import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.shared.exception.exercise.ExerciseNotFoundException;
import com.backend.gym.shared.exception.exercise.WorkoutExerciseNotFoundException;
import com.backend.gym.shared.exception.workout.WorkoutNotFoundException;
import com.backend.gym.workout.application.port.in.CreateExerciseToWorkoutUseCase;
import com.backend.gym.workout.application.port.in.DeleteExerciseFromWorkoutUseCase;
import com.backend.gym.workout.application.port.in.GetWorkoutExerciseUseCase;
import com.backend.gym.workout.application.port.in.UpdateWorkoutExerciseUseCase;
import com.backend.gym.workout.application.port.out.WorkoutExerciseRepositoryPort;
import com.backend.gym.workout.application.port.out.WorkoutRepositoryPort;
import com.backend.gym.workout.domain.Workout;
import com.backend.gym.workout.domain.WorkoutExercise;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkoutExerciseService implements
        CreateExerciseToWorkoutUseCase,
        GetWorkoutExerciseUseCase,
        UpdateWorkoutExerciseUseCase,
        DeleteExerciseFromWorkoutUseCase {

    private final WorkoutExerciseRepositoryPort repository;
    private final WorkoutRepositoryPort workoutRepository;
    private final ExerciseRepositoryPort exerciseRepository;

    @Override
    public WorkoutExercise execute(CreateExerciseToWorkoutCommand command) {
        Workout workout = workoutRepository.findById(command.workoutId())
            .orElseThrow(() -> new WorkoutNotFoundException(command.workoutId()));

        Exercise exercise = exerciseRepository.findById(command.exerciseId())
            .orElseThrow(() -> new ExerciseNotFoundException(command.exerciseId()));

        WorkoutExercise workoutExercise = new WorkoutExercise(
            null,
            workout,
            exercise,
            command.position(),
            command.targetSets(),
            command.targetReps(),
            command.notes()
        );
        return repository.save(workoutExercise);
    }

    @Override
    public WorkoutExercise findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new WorkoutExerciseNotFoundException(id));
    }

    @Override
    public Page<WorkoutExercise> findAllByWorkout(UUID workoutId, Pageable pageable) {
        return repository.findAllByWorkoutId(workoutId, pageable);
    }

    @Override
    public WorkoutExercise execute(UpdateWorkoutExerciseCommand command) {
        WorkoutExercise existing = findById(command.id());
        WorkoutExercise updated = new WorkoutExercise(
            existing.id(),
            existing.workout(),
            existing.exercise(),
            command.position(),
            command.targetSets(),
            command.targetReps(),
            command.notes()
        );
        return repository.save(updated);
    }

    @Override
    public void execute(UUID id) {
        findById(id);
        repository.deleteById(id);
    }
}