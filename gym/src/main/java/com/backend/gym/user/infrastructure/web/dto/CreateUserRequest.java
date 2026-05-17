package com.backend.gym.user.infrastructure.web.dto;

public record CreateUserRequest(
    String name,
    String email,
    String password
) {}
