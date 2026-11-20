package com.backend.gym.loadentry.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LoadEntryJpaRepository extends JpaRepository<LoadEntryEntity, UUID> {
    Optional<LoadEntryEntity> findTopByExerciseIdAndUserIdOrderByCreatedAtDesc(UUID exerciseId, UUID userId);
    @Query("SELECT DISTINCT l.performedAt FROM LoadEntryEntity l WHERE l.user.id = :userId ORDER BY l.performedAt DESC")
    List<LocalDate> findDistinctPerformedDatesByUserId(@Param("userId") UUID userId);
    @Query("SELECT l FROM LoadEntryEntity l WHERE l.user.id = :userId ORDER BY l.createdAt DESC LIMIT 5")
    List<LoadEntryEntity> findLastFiveByUserId(@Param("userId") UUID userId);
    @Query("""
        SELECT l FROM LoadEntryEntity l
        WHERE l.exercise.id IN :exerciseIds
        AND l.user.id = :userId
        AND l.workout IS NULL
        AND l.createdAt = (
            SELECT MAX(l2.createdAt) FROM LoadEntryEntity l2
            WHERE l2.exercise.id = l.exercise.id
                AND l2.user.id = :userId
                AND l2.workout IS NULL
        )
    """)
    List<LoadEntryEntity> findLastByExerciseIdInAndUserIdAndWorkoutIdIsNullOrderByCreatedAtDesc(
        @Param("exerciseIds") List<UUID> exerciseIds,
        @Param("userId") UUID userId
    );
    @Query("""
        SELECT l FROM LoadEntryEntity l
        WHERE l.exercise.id IN :exerciseIds
        AND l.user.id = :userId
        AND l.workout.id = :workoutId
        AND l.createdAt = (
            SELECT MAX(l2.createdAt) FROM LoadEntryEntity l2
            WHERE l2.exercise.id = l.exercise.id
                AND l2.user.id = :userId
                AND l2.workout.id = :workoutId
        )
    """)
    List<LoadEntryEntity> findLastByExerciseIdInAndUserIdAndWorkoutIdOrderByCreatedAtDesc(
        @Param("exerciseIds") List<UUID> exerciseIds,
        @Param("userId") UUID userId,
        @Param("workoutId") UUID workoutId
    );
    Page<LoadEntryEntity> findAllByUserId(UUID userId, Pageable pageable);
    Page<LoadEntryEntity> findAllByExerciseId(UUID exerciseId, Pageable pageable);
    Page<LoadEntryEntity> findAllByExerciseIdAndWorkoutId(UUID exerciseId, UUID workoutId, Pageable pageable);
    @Query("SELECT COUNT(DISTINCT l.exercise.id) FROM LoadEntryEntity l WHERE l.user.id = :userId AND MONTH(l.performedAt) = :month AND YEAR(l.performedAt) = :year")
    int countDistinctExercisesByUserIdAndMonth(@Param("userId") UUID userId, @Param("month") int month, @Param("year") int year);
    @Query("SELECT COALESCE(SUM(l.loadKg), 0) FROM LoadEntryEntity l WHERE l.user.id = :userId AND MONTH(l.performedAt) = :month AND YEAR(l.performedAt) = :year")
    BigDecimal sumLoadKgByUserIdAndMonth(@Param("userId") UUID userId, @Param("month") int month, @Param("year") int year);

}
