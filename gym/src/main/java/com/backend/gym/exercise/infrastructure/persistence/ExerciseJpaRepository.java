package com.backend.gym.exercise.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface ExerciseJpaRepository extends JpaRepository<ExerciseEntity, UUID>, JpaSpecificationExecutor<ExerciseEntity> {
    Page<ExerciseEntity> findAllByDeletedAtIsNull(Pageable pageable);
    @Query("SELECT e FROM ExerciseEntity e WHERE (e.createdByUser.id = :userId OR e.isDefault = true) AND e.deletedAt IS NULL")
    Page<ExerciseEntity> findAllByCreatedByUserIdOrIsDefaultAndDeletedAtIsNull(UUID userId, Pageable pageable);
}
