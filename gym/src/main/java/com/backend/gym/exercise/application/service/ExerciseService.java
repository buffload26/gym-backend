package com.backend.gym.exercise.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.gym.exercise.application.port.in.CreateExerciseUseCase;
import com.backend.gym.exercise.application.port.in.GetExerciseUseCase;
import com.backend.gym.exercise.application.port.in.SoftDeleteExerciseUseCase;
import com.backend.gym.exercise.application.port.in.UpdateExerciseUseCase;
import com.backend.gym.exercise.application.port.out.ExerciseRepositoryPort;
import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.shared.exception.exercise.ExerciseNotFoundException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExerciseService implements
        CreateExerciseUseCase,
        GetExerciseUseCase,
        UpdateExerciseUseCase,
        SoftDeleteExerciseUseCase {

    private final ExerciseRepositoryPort repository;
    private final UserRepositoryPort userRepository;

    @Override
    public Exercise execute(CreateExerciseCommand command) {
        User createdByUser = null;

        if (command.createdByUserId() != null) {
            createdByUser = userRepository.findById(command.createdByUserId())
                .orElseThrow(() -> new UserNotFoundException(command.createdByUserId()));
        }

        Exercise exercise = new Exercise(
            null,
            command.name(),
            command.description(),
            command.muscleGroup(),
            command.imageUrl(),
            command.videoUrl(),
            command.isDefault(),
            createdByUser,
            null,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        return repository.save(exercise);
    }

    @Override
    public Exercise findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ExerciseNotFoundException(id));
    }

    @Override
    public Page<Exercise> findAll(Pageable pageable) {
        return repository.findAllActive(pageable);
    }

    @Override
    public Page<Exercise> findAllByUser(UUID userId, Pageable pageable) {
        return repository.findAllByCreatedByUserIdorIsDefault(userId, pageable);
    }

    @Override
    public Exercise execute(UpdateExerciseCommand command) {
        Exercise existing = findById(command.id());
        Exercise updated = new Exercise(
            existing.id(),
            command.name(),
            command.description(),
            command.muscleGroup(),
            command.imageUrl(),
            command.videoUrl(),
            existing.isDefault(),
            existing.createdByUser(),
            existing.deletedAt(),
            existing.createdAt(),
            LocalDateTime.now()
        );
        return repository.save(updated);
    }

    @Override
    public void execute(UUID id) {
        Exercise existing = findById(id);
        Exercise deleted = new Exercise(
            existing.id(),
            existing.name(),
            existing.description(),
            existing.muscleGroup(),
            existing.imageUrl(),
            existing.videoUrl(),
            existing.isDefault(),
            existing.createdByUser(),  
            LocalDateTime.now(),
            existing.createdAt(),
            LocalDateTime.now()
        );
        repository.save(deleted);
    }
}