package com.backend.gym.notification.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.user.domain.User;

public record PushNotification(
    UUID id,
    User user,
    String title,
    String body,
    NotificationType type,
    UUID referenceId,
    LocalDateTime readAt,
    LocalDateTime createdAt
) {}