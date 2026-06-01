package com.backend.gym.workout.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.user.infrastructure.persistence.UserEntity;
import com.backend.gym.workout.domain.Workout;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workouts")
public class WorkoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Workout toDomain() {
        return new Workout(id, user.toDomain(), name, description, createdAt, updatedAt);
    }

    public static WorkoutEntity fromDomain(Workout workout) {
        return WorkoutEntity.builder()
            .id(workout.id())
            .user(UserEntity.fromDomain(workout.user()))
            .name(workout.name())
            .description(workout.description())
            .createdAt(workout.createdAt())
            .updatedAt(workout.updatedAt())
            .build();
    }
}
