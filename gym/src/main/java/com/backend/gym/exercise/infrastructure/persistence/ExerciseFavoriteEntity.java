package com.backend.gym.exercise.infrastructure.persistence;

import com.backend.gym.exercise.domain.ExerciseFavorite;
import com.backend.gym.user.infrastructure.persistence.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "exercise_favorites")
public class ExerciseFavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExerciseEntity exercise;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public ExerciseFavorite toDomain() {
        return new ExerciseFavorite(
            id,
            user.toDomain(),
            exercise.toDomain(),
            createdAt
        );
    }

    public static ExerciseFavoriteEntity fromDomain(ExerciseFavorite favorite,
                                                     UserEntity userEntity,
                                                     ExerciseEntity exerciseEntity) {
        return ExerciseFavoriteEntity.builder()
            .id(favorite.id())
            .user(userEntity)
            .exercise(exerciseEntity)
            .createdAt(favorite.createdAt())
            .build();
    }
}