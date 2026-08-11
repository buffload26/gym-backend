package com.backend.gym.workout.application.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.gym.exercise.application.port.out.ExerciseRepositoryPort;
import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.exercise.domain.ExerciseWithLoads;
import com.backend.gym.loadentry.application.port.out.LoadEntryRepositoryPort;
import com.backend.gym.loadentry.domain.LoadEntry;
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
    private final LoadEntryRepositoryPort loadEntryRepository;

    @Override
    public WorkoutExercise execute(CreateExerciseToWorkoutCommand command) {
        Workout workout = workoutRepository.findById(command.workoutId())
            .orElseThrow(() -> new WorkoutNotFoundException(command.workoutId()));

        Exercise exercise = exerciseRepository.findById(command.exerciseId())
            .orElseThrow(() -> new ExerciseNotFoundException(command.exerciseId()));

        WorkoutExercise workoutExercise = new WorkoutExercise(
            null,
            workout,
            new ExerciseWithLoads(exercise, List.of()),
            command.position(),
            command.targetSets(),
            command.targetReps(),
            command.notes(),
            command.sortOrder()
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
        Page<WorkoutExercise> workoutExercisePage = repository.findAllByWorkoutId(workoutId, pageable);

        List<UUID> exerciseIds = workoutExercisePage.getContent().stream()
            .map(we -> we.exercise().exercise().id())
            .toList();

        if (exerciseIds.isEmpty()) {
            return workoutExercisePage.map(we -> new WorkoutExercise(
                we.id(), we.workout(),
                new ExerciseWithLoads(we.exercise().exercise(), List.of()),
                we.position(), we.targetSets(), we.targetReps(), we.notes(), we.sortOrder()
            ));
        }

        List<LoadEntry> loads = loadEntryRepository.findAllByExerciseIdIn(exerciseIds);

        Map<UUID, List<LoadEntry>> loadsByExerciseId = loads.stream()
            .collect(Collectors.groupingBy(load -> load.exercise().id()));

        return workoutExercisePage.map(we -> {
            List<LoadEntry> exerciseLoads = loadsByExerciseId
                .getOrDefault(we.exercise().exercise().id(), List.of());

            return new WorkoutExercise(
                we.id(),
                we.workout(),
                new ExerciseWithLoads(we.exercise().exercise(), exerciseLoads),
                we.position(),
                we.targetSets(),
                we.targetReps(),
                we.notes(),
                we.sortOrder()
            );
        });
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
            command.notes(),
            command.sortOrder()
        );
        return repository.save(updated);
    }

    @Override
    public void execute(UUID id) {
        findById(id);
        repository.deleteById(id);
    }
}