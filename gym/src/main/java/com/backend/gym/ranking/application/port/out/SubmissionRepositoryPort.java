package com.backend.gym.ranking.application.port.out;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.ranking.domain.Submission;
import com.backend.gym.ranking.domain.SubmissionStatus;

public interface SubmissionRepositoryPort {

    Submission save(Submission submission);
    Optional<Submission> findById(UUID id);
    boolean existsByCompetitionIdAndUserId(UUID competitionId, UUID userId);
    Page<Submission> findAllByCompetitionId(UUID competitionId, Pageable pageable);
    Page<Submission> findAllByCompetitionIdAndStatus(UUID competitionId, SubmissionStatus status, Pageable pageable);
    void deleteById(UUID id);
}