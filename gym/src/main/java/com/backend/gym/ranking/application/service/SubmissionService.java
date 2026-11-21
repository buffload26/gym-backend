package com.backend.gym.ranking.application.service;


import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.gym.competition.application.port.out.CompetitionParticipantRepositoryPort;
import com.backend.gym.competition.application.port.out.CompetitionRepositoryPort;
import com.backend.gym.competition.domain.Competition;
import com.backend.gym.ranking.application.port.in.CreateSubmissionUseCase;
import com.backend.gym.ranking.application.port.in.GetSubmissionUseCase;
import com.backend.gym.ranking.application.port.out.SubmissionRepositoryPort;
import com.backend.gym.ranking.domain.Submission;
import com.backend.gym.ranking.domain.SubmissionStatus;
import com.backend.gym.shared.exception.competition.CompetitionNotFoundException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.storage.application.port.out.FileStoragePort;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionService implements
        CreateSubmissionUseCase,
        GetSubmissionUseCase {

    private final SubmissionRepositoryPort repository;
    private final CompetitionRepositoryPort competitionRepository;
    private final CompetitionParticipantRepositoryPort participantRepository;
    private final UserRepositoryPort userRepository;
    private final FileStoragePort fileStoragePort;

    @Override
    @Transactional
    public Submission execute(CreateSubmissionCommand command) {
        Competition competition = competitionRepository.findById(command.competitionId())
            .orElseThrow(() -> new CompetitionNotFoundException(command.competitionId()));

        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

        if (!participantRepository.existsByCompetitionIdAndUserId(
                command.competitionId(), command.userId())) {
            throw new IllegalStateException(
                "User is not a participant of this competition"
            );
        }

        if (repository.existsByCompetitionIdAndUserId(
                command.competitionId(), command.userId())) {
            throw new IllegalStateException(
                "User already has a submission for this competition"
            );
        }

        String videoUrl = fileStoragePort.upload(command.video());

        Submission submission = new Submission(
            null,
            competition,
            user,
            videoUrl,
            null,
            SubmissionStatus.PENDING,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        return repository.save(submission);
    }

    @Override
    @Transactional(readOnly = true)
    public Submission findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "Submission not found: " + id
            ));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Submission> findAllByCompetition(UUID competitionId, Pageable pageable) {
        return repository.findAllByCompetitionId(competitionId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Submission> findAllByCompetitionAndStatus(
            UUID competitionId,
            SubmissionStatus status,
            Pageable pageable) {
        return repository.findAllByCompetitionIdAndStatus(competitionId, status, pageable);
    }
}