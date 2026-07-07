package com.backend.gym.exercise.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.gym.exercise.application.port.in.CreateExerciseUseCase;
import com.backend.gym.exercise.application.port.in.GetExerciseUseCase;
import com.backend.gym.exercise.application.port.in.SoftDeleteExerciseUseCase;
import com.backend.gym.exercise.application.port.in.UpdateExerciseUseCase;
import com.backend.gym.exercise.application.port.out.ExerciseRepositoryPort;
import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.exercise.domain.ExerciseWithLoads;
import com.backend.gym.loadentry.application.port.out.LoadEntryRepositoryPort;
import com.backend.gym.loadentry.domain.LoadEntry;
import com.backend.gym.shared.exception.exercise.ExerciseNotFoundException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.storage.application.port.out.FileStoragePort;
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
    private final LoadEntryRepositoryPort loadEntryRepository;

    private final FileStoragePort fileStoragePort;

    @Override
    public Exercise execute(CreateExerciseCommand command) {
        User createdByUser = null;

        if (command.createdByUserId() != null) {
            createdByUser = userRepository.findById(command.createdByUserId())
                .orElseThrow(() -> new UserNotFoundException(command.createdByUserId()));
        }

        String imageUrl = null;
        if (command.image() != null && !command.image().isEmpty()) {
            imageUrl = fileStoragePort.upload(command.image());
        }

        Exercise exercise = new Exercise(
            null,
            command.name(),
            command.description(),
            command.muscleGroup(),
            imageUrl,
            command.videoUrl(),
            command.isDefault(),
            false,
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
    public Page<ExerciseWithLoads> findAllByFilter(ExerciseFilter filter, Pageable pageable) {
        Page<Exercise> exercisePage = repository.findAllByFilter(filter.userId(), filter.muscleGroup(), pageable);

        List<UUID> exerciseIds = exercisePage.getContent().stream()
            .map(Exercise::id)
            .toList();

        if (exerciseIds.isEmpty()) {
            return exercisePage.map(ex -> new ExerciseWithLoads(ex, List.of()));
        }

        List<LoadEntry> loads = loadEntryRepository.findAllByExerciseIdIn(exerciseIds);

        Map<UUID, List<LoadEntry>> loadsByExerciseId = loads.stream()
            .collect(Collectors.groupingBy(load -> load.exercise().id()));

        return exercisePage.map(exercise -> {
            List<LoadEntry> exerciseLoads = loadsByExerciseId.getOrDefault(exercise.id(), List.of());
            return new ExerciseWithLoads(exercise, exerciseLoads);
        });
    }

    @Override
    public Page<ExerciseWithLoads> findAllByUserWithLoads(UUID userId, Pageable pageable) {
        Page<Exercise> exercisePage = repository.findAllByCreatedByUserIdorIsDefault(userId, pageable);

        List<UUID> exerciseIds = exercisePage.getContent().stream()
            .map(Exercise::id)
            .toList();

        if (exerciseIds.isEmpty()) {
            return exercisePage.map(ex -> new ExerciseWithLoads(ex, List.of()));
        }

        List<LoadEntry> loads = loadEntryRepository.findAllByExerciseIdIn(exerciseIds);

        Map<UUID, List<LoadEntry>> loadsByExerciseId = loads.stream()
            .collect(Collectors.groupingBy(load -> load.exercise().id()));

        return exercisePage.map(exercise -> {
            List<LoadEntry> exerciseLoads = loadsByExerciseId.getOrDefault(exercise.id(), List.of());
            return new ExerciseWithLoads(exercise, exerciseLoads);
        });
    }

    @Override
    public Exercise execute(UpdateExerciseCommand command) {
        Exercise existing = findById(command.id());

        String newImageUrl = existing.imageUrl();
        
        if (command.image() != null && !command.image().isEmpty()) {
            newImageUrl = fileStoragePort.replaceFile(command.image(), existing.imageUrl());
            
        }

        Exercise updated = new Exercise(
            existing.id(),
            command.name(),
            command.description(),
            command.muscleGroup(),
            newImageUrl,
            command.videoUrl(),
            existing.isDefault(),
            command.isFavorite(),
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
            existing.isFavorite(),
            existing.createdByUser(),  
            LocalDateTime.now(),
            existing.createdAt(),
            LocalDateTime.now()
        );
        repository.save(deleted);
    }
}