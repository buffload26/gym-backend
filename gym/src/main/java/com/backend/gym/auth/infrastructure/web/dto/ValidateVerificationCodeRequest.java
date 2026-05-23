package com.backend.gym.auth.infrastructure.web.dto;

public record ValidateVerificationCodeRequest(String email, String code) {}
