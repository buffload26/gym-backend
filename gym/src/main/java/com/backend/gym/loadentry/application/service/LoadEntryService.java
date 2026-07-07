package com.backend.gym.loadentry.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.gym.exercise.application.port.out.ExerciseRepositoryPort;
import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.loadentry.application.port.in.CreateLoadEntryUseCase;
import com.backend.gym.loadentry.application.port.in.DeleteLoadEntryUseCase;
import com.backend.gym.loadentry.application.port.in.GetLoadEntryUseCase;
import com.backend.gym.loadentry.application.port.in.UpdateLoadEntryUseCase;
import com.backend.gym.loadentry.application.port.out.LoadEntryRepositoryPort;
import com.backend.gym.loadentry.domain.LoadEntry;
import com.backend.gym.shared.exception.exercise.ExerciseNotFoundException;
import com.backend.gym.shared.exception.loadentry.LoadEntryNotFoundException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.shared.exception.workout.WorkoutNotFoundException;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;
import com.backend.gym.workout.application.port.out.WorkoutRepositoryPort;
import com.backend.gym.workout.domain.Workout;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoadEntryService implements
        CreateLoadEntryUseCase,
        GetLoadEntryUseCase,
        UpdateLoadEntryUseCase,
        DeleteLoadEntryUseCase {

    private final LoadEntryRepositoryPort repository;
    private final UserRepositoryPort userRepository;
    private final ExerciseRepositoryPort exerciseRepository;
    private final WorkoutRepositoryPort workoutRepository;

    @Override
    public LoadEntry execute(CreateLoadEntryCommand command) {
        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

        Exercise exercise = exerciseRepository.findById(command.exerciseId())
            .orElseThrow(() -> new ExerciseNotFoundException(command.exerciseId()));

        Workout workout = null;
        if (command.workoutId() != null) {
            workout = workoutRepository.findById(command.workoutId())
                .orElseThrow(() -> new WorkoutNotFoundException(command.workoutId()));
        }

        BigDecimal warmupLoadKg = repository
            .findLastByExerciseIdAndUserId(command.exerciseId(), command.userId())
            .map(LoadEntry::warmupLoadKg)
            .orElse(null);

        LoadEntry loadEntry = new LoadEntry(
            null,
            user,
            exercise,
            workout,
            command.performedAt(),
            command.loadKg(),
            warmupLoadKg,
            null,
            command.sets(),
            command.reps(),
            command.notes(),
            LocalDateTime.now()
        );
        return repository.save(loadEntry);
    }

    @Override
    public LoadEntry findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new LoadEntryNotFoundException(id));
    }

    @Override
    public Page<LoadEntry> findAllByUser(UUID userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable);
    }

    @Override
    public Page<LoadEntry> findAllByExercise(UUID exerciseId, Pageable pageable) {
        return repository.findAllByExerciseId(exerciseId, pageable);
    }

    @Override
    public LoadEntry execute(UpdateLoadEntryCommand command) {
        LoadEntry existing = findById(command.id());

        LoadEntry updated = new LoadEntry(
            existing.id(),
            existing.user(),
            existing.exercise(),
            existing.workout(),
            command.performedAt(),
            command.loadKg(),
            command.warmupLoadKg(),
            command.nextLoadKg(),
            command.sets(),
            command.reps(),
            command.notes(),
            existing.createdAt()
        );

        return repository.save(updated);
    }

    @Override
    public void execute(UUID id) {
        findById(id);
        repository.deleteById(id);
    }
}
