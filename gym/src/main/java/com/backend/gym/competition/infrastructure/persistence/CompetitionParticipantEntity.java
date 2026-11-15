
package com.backend.gym.competition.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.competition.domain.CompetitionParticipant;
import com.backend.gym.competition.domain.CompetitionParticipantStatus;
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
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "competition_participants",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"competition_id", "user_id"}
        )
    }
)
public class CompetitionParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competition_id", nullable = false)
    private CompetitionEntity competition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CompetitionParticipantStatus status;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private LocalDateTime joinedAt;

    @Column(name = "invited_at")
    private LocalDateTime invitedAt;

    public CompetitionParticipant toDomain() {
        return new CompetitionParticipant(
            id,
            competition != null ? competition.toDomain() : null,
            user != null ? user.toDomain() : null,
            status,
            joinedAt,
            invitedAt
        );
    }

    public static CompetitionParticipantEntity fromDomain(CompetitionParticipant participant) {
        return CompetitionParticipantEntity.builder()
            .id(participant.id())
            .competition(
                participant.competition() != null
                    ? CompetitionEntity.fromDomain(participant.competition())
                    : null
            )
            .user(
                participant.user() != null
                    ? UserEntity.fromDomain(participant.user())
                    : null
            )
            .status(participant.status())
            .joinedAt(participant.joinedAt())
            .invitedAt(participant.invitedAt())
            .build();
    }
}
