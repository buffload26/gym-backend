package com.backend.gym.exercise.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExerciseJpaRepository extends JpaRepository<ExerciseEntity, UUID>, JpaSpecificationExecutor<ExerciseEntity> {
    @Query(value = """
        SELECT * FROM exercises e
        WHERE e.deleted_at IS NULL
        AND e.is_favorite = true
        AND e.created_by_user_id = :userId
        """, nativeQuery = true)
    List<ExerciseEntity> findFavoritesByUserId(@Param("userId") UUID userId);
    Page<ExerciseEntity> findAllByDeletedAtIsNull(Pageable pageable);
    @Query("SELECT e FROM ExerciseEntity e WHERE (e.createdByUser.id = :userId OR e.isDefault = true) AND e.deletedAt IS NULL")
    Page<ExerciseEntity> findAllByCreatedByUserIdOrIsDefaultAndDeletedAtIsNull(UUID userId, Pageable pageable);
}
