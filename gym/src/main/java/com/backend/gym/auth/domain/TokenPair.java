package com.backend.gym.auth.domain;

public record TokenPair(
    String accessToken,
    String refreshToken
) {}