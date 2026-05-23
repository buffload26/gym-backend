package com.backend.gym.auth.infrastructure.web.dto;

public record UpdatePasswordRequest(String email, String code, String newPassword) {}

