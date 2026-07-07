package com.backend.gym.exercise.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.user.infrastructure.persistence.UserEntity;

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
@Table(name = "exercises")
public class ExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "muscle_group", length = 80)
    private String muscleGroup;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @Column(name = "video_url", columnDefinition = "TEXT")
    private String videoUrl;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    @Column(name = "is_favorite", nullable = false)
    private boolean isFavorite;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private UserEntity createdByUser;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Exercise toDomain() {
        return new Exercise(
            id, name, description, muscleGroup,
            imageUrl, videoUrl, isDefault, isFavorite,
            createdByUser != null ? createdByUser.toDomain() : null,
            deletedAt, createdAt, updatedAt
        );
    }

    public static ExerciseEntity fromDomain(Exercise exercise) {
        return ExerciseEntity.builder()
            .id(exercise.id())
            .name(exercise.name())
            .description(exercise.description())
            .muscleGroup(exercise.muscleGroup())
            .imageUrl(exercise.imageUrl())
            .videoUrl(exercise.videoUrl())
            .isDefault(exercise.isDefault())
            .isFavorite(exercise.isFavorite())
            .createdByUser(exercise.createdByUser() != null ? UserEntity.fromDomain(exercise.createdByUser()) : null)
            .deletedAt(exercise.deletedAt())
            .createdAt(exercise.createdAt())
            .updatedAt(exercise.updatedAt())
            .build();
    }
}