package com.backend.gym.ranking.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.competition.infrastructure.persistence.CompetitionEntity;
import com.backend.gym.ranking.domain.Submission;
import com.backend.gym.ranking.domain.SubmissionStatus;
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
@Table(name = "submissions")
public class SubmissionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private CompetitionEntity competition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "video_url", columnDefinition = "TEXT", nullable = false)
    private String videoUrl;

    @Column(name = "video_deleted_at")
    private LocalDateTime videoDeletedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SubmissionStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Submission toDomain() {
        return new Submission(
            id,
            competition.toDomain(),
            user.toDomain(),
            videoUrl,
            videoDeletedAt,
            status,
            createdAt,
            updatedAt
        );
    }

    public static SubmissionEntity fromDomain(Submission submission) {
        return SubmissionEntity.builder()
            .id(submission.id())
            .competition(CompetitionEntity.fromDomain(submission.competition()))
            .user(UserEntity.fromDomain(submission.user()))
            .videoUrl(submission.videoUrl())
            .videoDeletedAt(submission.videoDeletedAt())
            .status(submission.status())
            .createdAt(submission.createdAt())
            .updatedAt(submission.updatedAt())
            .build();
    }
}