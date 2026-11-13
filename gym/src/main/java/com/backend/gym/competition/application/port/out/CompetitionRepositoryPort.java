package com.backend.gym.competition.application.port.out;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.competition.domain.Competition;
import com.backend.gym.competition.domain.CompetitionStatus;

public interface CompetitionRepositoryPort {

    Competition save(Competition competition);
    Optional<Competition> findById(UUID id);
    Page<Competition> findAllActive(Pageable pageable);
    Page<Competition> findAllByFilter(
        UUID userId,
        UUID exerciseId,
        CompetitionStatus status,
        Pageable pageable
    );
}