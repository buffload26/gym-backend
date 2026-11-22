package com.backend.gym.submission.application.port.in;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.backend.gym.submission.domain.Submission;

public interface CreateSubmissionUseCase {

    Submission execute(CreateSubmissionCommand command);

    record CreateSubmissionCommand(
        UUID competitionId,
        UUID userId,
        MultipartFile video,
        BigDecimal loadKg,
        Integer repetitions,
        String observation
    ) {}
}