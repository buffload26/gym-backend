
package com.backend.gym.competition.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.competition.domain.Competition;
import com.backend.gym.competition.domain.CompetitionStatus;
import com.backend.gym.exercise.infrastructure.persistence.ExerciseEntity;
import com.backend.gym.user.infrastructure.persistence.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "competitions")
public class CompetitionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String rules;

    @Column(name = "image_url", columnDefinition = "TEXT")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExerciseEntity exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private UserEntity createdByUser;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CompetitionStatus status;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Competition toDomain() {
        return new Competition(
            id,
            title,
            description,
            rules,
            imageUrl,
            exercise != null ? exercise.toDomain() : null,
            createdByUser != null ? createdByUser.toDomain() : null,
            status,
            startAt,
            endAt,
            deletedAt,
            createdAt,
            updatedAt
        );
    }

    public static CompetitionEntity fromDomain(Competition competition) {
        return CompetitionEntity.builder()
            .id(competition.id())
            .title(competition.title())
            .description(competition.description())
            .rules(competition.rules())
            .imageUrl(competition.imageUrl())
            .exercise(
                competition.exercise() != null
                    ? ExerciseEntity.fromDomain(competition.exercise())
                    : null
            )
            .createdByUser(
                competition.createdByUser() != null
                    ? UserEntity.fromDomain(competition.createdByUser())
                    : null
            )
            .status(competition.status())
            .startAt(competition.startAt())
            .endAt(competition.endAt())
            .deletedAt(competition.deletedAt())
            .createdAt(competition.createdAt())
            .updatedAt(competition.updatedAt())
            .build();
    }
}
