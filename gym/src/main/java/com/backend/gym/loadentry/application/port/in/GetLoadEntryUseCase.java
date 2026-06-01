package com.backend.gym.loadentry.application.port.in;

import java.util.List;
import java.util.UUID;

import com.backend.gym.loadentry.domain.LoadEntry;

public interface GetLoadEntryUseCase {
    LoadEntry findById(UUID id);
    List<LoadEntry> findAllByUser(UUID userId);
    List<LoadEntry> findAllByExercise(UUID exerciseId);
}