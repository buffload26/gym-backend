package com.backend.gym.ranking.application.port.in;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.backend.gym.ranking.domain.Submission;

public interface CreateSubmissionUseCase {

    Submission execute(CreateSubmissionCommand command);

    record CreateSubmissionCommand(
        UUID competitionId,
        UUID userId,
        MultipartFile video
    ) {}
}