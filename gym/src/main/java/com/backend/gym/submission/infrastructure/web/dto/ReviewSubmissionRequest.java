package com.backend.gym.submission.infrastructure.web.dto;

import java.util.UUID;

public record ReviewSubmissionRequest(
    UUID voterUserId,
    boolean accepted
) {}