package com.backend.gym.notification.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.user.domain.User;

public record UserDeviceToken(
    UUID id,
    User user,
    String token,
    DeviceType deviceType,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}