package com.backend.gym.competition.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.competition.domain.Competition;
import com.backend.gym.competition.domain.CompetitionStatus;
import com.backend.gym.competition.domain.CompetitionWithParticipants;

public interface GetCompetitionUseCase {

    Competition findById(UUID id);
    CompetitionWithParticipants findByIdWithParticipants(UUID id);
    Page<Competition> findAll(Pageable pageable);
    Page<Competition> findAllByFilter(CompetitionFilter filter, Pageable pageable);

    record CompetitionFilter(
        UUID userId,
        UUID exerciseId,
        CompetitionStatus status
    ) {}
}