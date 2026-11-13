package com.backend.gym.competition.application.port.in;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.backend.gym.competition.domain.Competition;

public interface CreateCompetitionUseCase {

    Competition execute(CreateCompetitionCommand command);

    record CreateCompetitionCommand(
        String title,
        String description,
        String rules,
        MultipartFile image,
        UUID exerciseId,
        UUID createdByUserId,
        LocalDateTime startAt,
        LocalDateTime endAt
    ) {}
}