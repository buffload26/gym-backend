package com.backend.gym.exercise.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseJpaRepository extends JpaRepository<ExerciseEntity, UUID> {
    Page<ExerciseEntity> findAllByDeletedAtIsNull(Pageable pageable);
    Page<ExerciseEntity> findAllByCreatedByUserIdAndDeletedAtIsNull(UUID userId, Pageable pageable);
}
