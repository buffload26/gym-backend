package com.backend.gym.ranking.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.ranking.domain.Submission;
import com.backend.gym.ranking.domain.SubmissionStatus;

public interface GetSubmissionUseCase {

    Submission findById(UUID id);
    Page<Submission> findAllByCompetition(UUID competitionId, Pageable pageable);
    Page<Submission> findAllByCompetitionAndStatus(UUID competitionId, SubmissionStatus status, Pageable pageable);
}