package com.backend.gym.loadentry.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoadEntryJpaRepository extends JpaRepository<LoadEntryEntity, UUID> {
    Page<LoadEntryEntity> findAllByUserId(UUID userId, Pageable pageable);
    Page<LoadEntryEntity> findAllByExerciseId(UUID exerciseId, Pageable pageable);
}
