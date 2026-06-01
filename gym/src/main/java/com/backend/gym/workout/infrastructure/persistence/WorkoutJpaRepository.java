package com.backend.gym.workout.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkoutJpaRepository extends JpaRepository<WorkoutEntity, UUID> {
    List<WorkoutEntity> findAllByUserId(UUID userId);
}
