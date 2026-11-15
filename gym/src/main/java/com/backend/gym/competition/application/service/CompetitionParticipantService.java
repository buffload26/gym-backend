package com.backend.gym.competition.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.gym.competition.application.port.in.AcceptCompetitionInviteUseCase;
import com.backend.gym.competition.application.port.in.GetCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.InviteCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.JoinCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.LeaveCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.RejectCompetitionInviteUseCase;
import com.backend.gym.competition.application.port.out.CompetitionParticipantRepositoryPort;
import com.backend.gym.competition.application.port.out.CompetitionRepositoryPort;
import com.backend.gym.competition.domain.Competition;
import com.backend.gym.competition.domain.CompetitionParticipant;
import com.backend.gym.competition.domain.CompetitionParticipantStatus;
import com.backend.gym.competition.domain.CompetitionStatus;
import com.backend.gym.notification.application.port.in.SendPushNotificationUseCase;
import com.backend.gym.notification.application.port.in.SendPushNotificationUseCase.SendPushNotificationCommand;
import com.backend.gym.notification.domain.NotificationType;
import com.backend.gym.shared.exception.competition.CompetitionInviteNotPendingException;
import com.backend.gym.shared.exception.competition.CompetitionNotFoundException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompetitionParticipantService implements
        JoinCompetitionParticipantUseCase,
        InviteCompetitionParticipantUseCase,
        AcceptCompetitionInviteUseCase,
        RejectCompetitionInviteUseCase,
        GetCompetitionParticipantUseCase,
        LeaveCompetitionParticipantUseCase {

    private final CompetitionParticipantRepositoryPort repository;
    private final CompetitionRepositoryPort competitionRepository;
    private final UserRepositoryPort userRepository;
    
    private final SendPushNotificationUseCase sendPushNotificationUseCase;

    @Override
    @Transactional
    public CompetitionParticipant execute(JoinCompetitionParticipantCommand command) {
        Competition competition = competitionRepository.findById(command.competitionId())
            .orElseThrow(() -> new CompetitionNotFoundException(command.competitionId()));

        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

        if (competition.status() != CompetitionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot join a finished competition");
        }

        if (repository.existsByCompetitionIdAndUserId(
                command.competitionId(), command.userId())) {
            throw new IllegalStateException(
                "User is already participating in this competition"
            );
        }

        CompetitionParticipant participant = new CompetitionParticipant(
            null,
            competition,
            user,
            CompetitionParticipantStatus.ACCEPTED,
            LocalDateTime.now(),
            null
        );

        return repository.save(participant);
    }

    @Override
    @Transactional
    public CompetitionParticipant execute(InviteCompetitionParticipantCommand command) {
        Competition competition = competitionRepository.findById(command.competitionId())
            .orElseThrow(() -> new CompetitionNotFoundException(command.competitionId()));

        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

        if (competition.status() != CompetitionStatus.ACTIVE) {
            throw new IllegalStateException("Cannot invite to a finished competition");
        }

        if (repository.existsByCompetitionIdAndUserId(
                command.competitionId(), command.userId())) {
            throw new IllegalStateException(
                "User is already participating in this competition"
            );
        }

        CompetitionParticipant participant = new CompetitionParticipant(
            null,
            competition,
            user,
            CompetitionParticipantStatus.PENDING,
            null,
            LocalDateTime.now()
        );

        CompetitionParticipant saved = repository.save(participant);

        sendPushNotificationUseCase.execute(new SendPushNotificationCommand(
            command.userId(),
            "Competition Invite",
            "You have been invited to join: " + competition.title(),
            NotificationType.COMPETITION_INVITE,
            command.competitionId()
        ));

        return saved;
    }

    @Override
    @Transactional
    public CompetitionParticipant accept(UUID competitionId, UUID userId) {
        CompetitionParticipant existing = repository
            .findByCompetitionIdAndUserId(competitionId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Invite not found"));

        if (existing.status() != CompetitionParticipantStatus.PENDING) {
            throw new CompetitionInviteNotPendingException();
        }

        CompetitionParticipant accepted = new CompetitionParticipant(
            existing.id(),
            existing.competition(),
            existing.user(),
            CompetitionParticipantStatus.ACCEPTED,
            LocalDateTime.now(),
            existing.invitedAt()
        );

        return repository.save(accepted);
    }

    @Override
    @Transactional
    public CompetitionParticipant reject(UUID competitionId, UUID userId) {
        CompetitionParticipant existing = repository
            .findByCompetitionIdAndUserId(competitionId, userId)
            .orElseThrow(() -> new IllegalArgumentException("Invite not found"));

        if (existing.status() != CompetitionParticipantStatus.PENDING) {
            throw new CompetitionInviteNotPendingException();
        }

        CompetitionParticipant rejected = new CompetitionParticipant(
            existing.id(),
            existing.competition(),
            existing.user(),
            CompetitionParticipantStatus.REJECTED,
            existing.joinedAt(),
            existing.invitedAt()
        );

        return repository.save(rejected);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCompetitionAndUser(UUID competitionId, UUID userId) {
        return repository.existsByCompetitionIdAndUserId(competitionId, userId);
    }

    @Override
    @Transactional
    public void execute(UUID competitionId, UUID userId) {
        if (!repository.existsByCompetitionIdAndUserId(competitionId, userId)) {
            throw new IllegalArgumentException(
                "User is not participating in this competition"
            );
        }

        repository.deleteByCompetitionIdAndUserId(competitionId, userId);
    }
}