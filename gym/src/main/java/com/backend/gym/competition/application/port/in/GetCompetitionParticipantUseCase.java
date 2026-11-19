package com.backend.gym.competition.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.competition.domain.CompetitionParticipant;
import com.backend.gym.competition.domain.CompetitionParticipantStatus;

public interface GetCompetitionParticipantUseCase {

    
    boolean existsByCompetitionAndUser(
        UUID competitionId,
        UUID userId
    );
    
    Page<CompetitionParticipant> findAllByFilter(
        CompetitionParticipantFilter filter,
        Pageable pageable
    );
    record CompetitionParticipantFilter(
        UUID competitionId,
        UUID userId,
        CompetitionParticipantStatus status
    ) {}
}