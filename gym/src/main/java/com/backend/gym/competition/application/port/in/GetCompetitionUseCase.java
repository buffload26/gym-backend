package com.backend.gym.competition.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.competition.domain.CompetitionStatus;
import com.backend.gym.competition.domain.CompetitionWithParticipantCount;
import com.backend.gym.competition.domain.CompetitionWithParticipants;

public interface GetCompetitionUseCase {

    CompetitionWithParticipantCount findById(UUID id);
    CompetitionWithParticipants findByIdWithParticipants(UUID id);
    Page<CompetitionWithParticipantCount> findAll(Pageable pageable);
    Page<CompetitionWithParticipantCount> findAllByFilter(CompetitionFilter filter, Pageable pageable);

    record CompetitionFilter(
        UUID userId,
        UUID exerciseId,
        CompetitionStatus status
    ) {}
}