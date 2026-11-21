package com.backend.gym.ranking.infrastructure.web.dto;

import java.util.UUID;

public record ReviewSubmissionRequest(
    UUID voterUserId,
    boolean accepted
) {}