package com.backend.gym.submission.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.submission.domain.SubmissionVote;
import com.backend.gym.submission.domain.SubmissionVoteType;
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
@Table(name = "submission_votes")
public class SubmissionVoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "submission_id", nullable = false)
    private SubmissionEntity submission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_user_id", nullable = false)
    private UserEntity voter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SubmissionVoteType vote;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public SubmissionVote toDomain() {
        return new SubmissionVote(
            id,
            submission.toDomain(),
            voter.toDomain(),
            vote,
            createdAt
        );
    }

    public static SubmissionVoteEntity fromDomain(SubmissionVote vote) {
        return SubmissionVoteEntity.builder()
            .id(vote.id())
            .submission(SubmissionEntity.fromDomain(vote.submission()))
            .voter(UserEntity.fromDomain(vote.voter()))
            .vote(vote.vote())
            .createdAt(vote.createdAt())
            .build();
    }
}