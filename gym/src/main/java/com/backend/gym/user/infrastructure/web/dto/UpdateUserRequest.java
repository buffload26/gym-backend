package com.backend.gym.user.infrastructure.web.dto;

public record UpdateUserRequest(
    String name,
    String email
) {}