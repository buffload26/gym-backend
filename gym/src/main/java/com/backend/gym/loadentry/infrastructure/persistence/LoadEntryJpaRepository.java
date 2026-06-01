package com.backend.gym.loadentry.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoadEntryJpaRepository extends JpaRepository<LoadEntryEntity, UUID> {
    List<LoadEntryEntity> findAllByUserId(UUID userId);
    List<LoadEntryEntity> findAllByExerciseId(UUID exerciseId);
}
