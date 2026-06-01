package com.backend.gym.loadentry.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.backend.gym.loadentry.domain.LoadEntry;

public interface LoadEntryRepositoryPort {
    LoadEntry save(LoadEntry loadEntry);
    Optional<LoadEntry> findById(UUID id);
    List<LoadEntry> findAllByUserId(UUID userId);
    List<LoadEntry> findAllByExerciseId(UUID exerciseId);
    void deleteById(UUID id);
}