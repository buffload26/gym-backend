
package com.backend.gym.competition.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.gym.competition.application.port.in.GetCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.JoinCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.LeaveCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.out.CompetitionParticipantRepositoryPort;
import com.backend.gym.competition.application.port.out.CompetitionRepositoryPort;
import com.backend.gym.competition.domain.Competition;
import com.backend.gym.competition.domain.CompetitionParticipant;
import com.backend.gym.competition.domain.CompetitionStatus;
import com.backend.gym.shared.exception.competition.CompetitionNotFoundException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompetitionParticipantService implements
        JoinCompetitionParticipantUseCase,
        GetCompetitionParticipantUseCase,
        LeaveCompetitionParticipantUseCase {

    private final CompetitionParticipantRepositoryPort repository;
    private final CompetitionRepositoryPort competitionRepository;
    private final UserRepositoryPort userRepository;

    @Override
    @Transactional
    public CompetitionParticipant execute(JoinCompetitionParticipantCommand command) {
        Competition competition = competitionRepository.findById(command.competitionId())
            .orElseThrow(() -> new CompetitionNotFoundException(command.competitionId()));

        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

        if (competition.status() != CompetitionStatus.ACTIVE) {
            throw new IllegalStateException(
                "Cannot join a finished competition"
            );
        }

        if (repository.existsByCompetitionIdAndUserId(
                command.competitionId(),
                command.userId())) {
            throw new IllegalStateException(
                "User is already participating in this competition"
            );
        }

        CompetitionParticipant participant = new CompetitionParticipant(
            null,
            competition,
            user,
            LocalDateTime.now()
        );

        return repository.save(participant);
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
