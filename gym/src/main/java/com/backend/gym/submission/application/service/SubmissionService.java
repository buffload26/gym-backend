package com.backend.gym.submission.application.service;


import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.gym.competition.application.port.out.CompetitionParticipantRepositoryPort;
import com.backend.gym.competition.application.port.out.CompetitionRepositoryPort;
import com.backend.gym.competition.domain.Competition;
import com.backend.gym.notification.application.port.in.SendPushNotificationUseCase;
import com.backend.gym.notification.application.port.in.SendPushNotificationUseCase.SendPushNotificationCommand;
import com.backend.gym.notification.domain.NotificationType;
import com.backend.gym.shared.exception.competition.CompetitionNotFoundException;
import com.backend.gym.shared.exception.ranking.SubmissionAlreadyExistsException;
import com.backend.gym.shared.exception.ranking.SubmissionVoterNotParticipantException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.storage.application.port.out.FileStoragePort;
import com.backend.gym.submission.application.port.in.CreateSubmissionUseCase;
import com.backend.gym.submission.application.port.in.GetSubmissionUseCase;
import com.backend.gym.submission.application.port.out.SubmissionRepositoryPort;
import com.backend.gym.submission.domain.Submission;
import com.backend.gym.submission.domain.SubmissionStatus;
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

    private final SendPushNotificationUseCase sendPushNotificationUseCase;

    @Override
    @Transactional
    public Submission execute(CreateSubmissionCommand command) {
        Competition competition = competitionRepository.findById(command.competitionId())
            .orElseThrow(() -> new CompetitionNotFoundException(command.competitionId()));

        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

        if (!participantRepository.existsByCompetitionIdAndUserId(
                command.competitionId(), command.userId())) {
            throw new SubmissionVoterNotParticipantException();
        }

        if (repository.existsByCompetitionIdAndUserId(
                command.competitionId(), command.userId())) {
            throw new SubmissionAlreadyExistsException();
        }

        String videoUrl = fileStoragePort.upload(command.video());

        Submission submission = new Submission(
            null,
            competition,
            user,
            videoUrl,
            null,
            SubmissionStatus.PENDING,
            command.loadKg(),
            command.repetitions(),
            command.observation(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Submission saved = repository.save(submission);

        participantRepository.findAllByCompetitionId(command.competitionId())
            .stream()
            .filter(p -> !p.user().id().equals(command.userId()))
            .forEach(p -> sendPushNotificationUseCase.execute(new SendPushNotificationCommand(
                p.user().id(),
                "New Submission",
                user.name() + " submitted a new entry in " + competition.title(),
                NotificationType.SUBMISSION_CREATED,
                saved.id()
            )));

        return saved;
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