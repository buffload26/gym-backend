package com.backend.gym.competition.application.port.in;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.backend.gym.competition.domain.Competition;

public interface UpdateCompetitionUseCase {

    Competition execute(UpdateCompetitionCommand command);

    record UpdateCompetitionCommand(
        UUID id,
        String title,
        String description,
        String rules,
        MultipartFile image,
        LocalDateTime startAt,
        LocalDateTime endAt
    ) {}
}