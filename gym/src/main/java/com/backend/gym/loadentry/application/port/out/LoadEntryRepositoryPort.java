package com.backend.gym.loadentry.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.loadentry.domain.LoadEntry;

public interface LoadEntryRepositoryPort {
    LoadEntry save(LoadEntry loadEntry);
    Optional<LoadEntry> findById(UUID id);
    Optional<LoadEntry> findLastByExerciseIdAndUserId(UUID exerciseId, UUID userId);
    Page<LoadEntry> findAllByUserId(UUID userId, Pageable pageable);
    Page<LoadEntry> findAllByExerciseId(UUID exerciseId, Pageable pageable);
    List<LoadEntry> findAllByExerciseIdIn(List<UUID> exerciseIds);
    void deleteById(UUID id);
}