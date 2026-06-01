package com.backend.gym.loadentry.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.loadentry.domain.LoadEntry;

public interface GetLoadEntryUseCase {
    LoadEntry findById(UUID id);
    Page<LoadEntry> findAllByUser(UUID userId, Pageable pageable);
    Page<LoadEntry> findAllByExercise(UUID exerciseId, Pageable pageable);
}