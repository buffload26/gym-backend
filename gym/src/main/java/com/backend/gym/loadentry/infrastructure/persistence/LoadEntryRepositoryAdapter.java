package com.backend.gym.loadentry.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.backend.gym.exercise.infrastructure.persistence.ExerciseEntity;
import com.backend.gym.exercise.infrastructure.persistence.ExerciseJpaRepository;
import com.backend.gym.loadentry.application.port.out.LoadEntryRepositoryPort;
import com.backend.gym.loadentry.domain.LoadEntry;
import com.backend.gym.shared.exception.exercise.ExerciseNotFoundException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.shared.exception.workout.WorkoutNotFoundException;
import com.backend.gym.user.infrastructure.persistence.UserEntity;
import com.backend.gym.user.infrastructure.persistence.UserJpaRepository;
import com.backend.gym.workout.infrastructure.persistence.WorkoutEntity;
import com.backend.gym.workout.infrastructure.persistence.WorkoutJpaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LoadEntryRepositoryAdapter implements LoadEntryRepositoryPort {

    private final LoadEntryJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ExerciseJpaRepository exerciseJpaRepository;
    private final WorkoutJpaRepository workoutJpaRepository;

    @Override
    public LoadEntry save(LoadEntry loadEntry) {
        UserEntity userEntity = userJpaRepository.findById(loadEntry.user().id())
            .orElseThrow(() -> new UserNotFoundException(loadEntry.user().id()));

        ExerciseEntity exerciseEntity = exerciseJpaRepository.findById(loadEntry.exercise().id())
            .orElseThrow(() -> new ExerciseNotFoundException(loadEntry.exercise().id()));

        WorkoutEntity workoutEntity = null;
        if (loadEntry.workout() != null) {
            workoutEntity = workoutJpaRepository.findById(loadEntry.workout().id())
                .orElseThrow(() -> new WorkoutNotFoundException(loadEntry.workout().id()));
        }

        return jpaRepository.save(
            LoadEntryEntity.fromDomain(loadEntry, userEntity, exerciseEntity, workoutEntity)
        ).toDomain();
    }

    @Override
    public Optional<LoadEntry> findById(UUID id) {
        return jpaRepository.findById(id).map(LoadEntryEntity::toDomain);
    }

    @Override
    public List<LoadEntry> findAllByExerciseIdIn(List<UUID> exerciseIds) {
        return jpaRepository.findAllByExerciseIdIn(exerciseIds)
            .stream()
            .map(LoadEntryEntity::toDomain)
            .toList();
    }

    @Override
    public Page<LoadEntry> findAllByUserId(UUID userId, Pageable pageable) {
        return jpaRepository.findAllByUserId(userId, pageable)
            .map(LoadEntryEntity::toDomain);
    }

    @Override
    public Page<LoadEntry> findAllByExerciseId(UUID exerciseId, Pageable pageable) {
        return jpaRepository.findAllByExerciseId(exerciseId, pageable)
            .map(LoadEntryEntity::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
