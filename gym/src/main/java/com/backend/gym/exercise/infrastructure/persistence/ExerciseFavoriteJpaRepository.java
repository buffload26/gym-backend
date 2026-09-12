package com.backend.gym.exercise.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ExerciseFavoriteJpaRepository extends JpaRepository<ExerciseFavoriteEntity, UUID> {
    List<ExerciseFavoriteEntity> findAllByUserId(UUID userId);
    Optional<ExerciseFavoriteEntity> findByUserIdAndExerciseId(UUID userId, UUID exerciseId);
    boolean existsByUserIdAndExerciseId(UUID userId, UUID exerciseId);
    void deleteByUserIdAndExerciseId(UUID userId, UUID exerciseId);
}