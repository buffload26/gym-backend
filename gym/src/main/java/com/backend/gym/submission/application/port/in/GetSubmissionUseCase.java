package com.backend.gym.submission.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.submission.domain.Submission;
import com.backend.gym.submission.domain.SubmissionStatus;

public interface GetSubmissionUseCase {

    Submission findById(UUID id);
    Page<Submission> findAllByCompetition(UUID competitionId, Pageable pageable);
    Page<Submission> findAllByCompetitionAndStatus(UUID competitionId, SubmissionStatus status, Pageable pageable);
    Page<Submission> findAcceptedRanking(UUID competitionId, Pageable pageable);
}