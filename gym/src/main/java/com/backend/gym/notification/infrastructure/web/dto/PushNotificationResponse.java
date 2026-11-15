package com.backend.gym.notification.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.notification.domain.NotificationType;
import com.backend.gym.notification.domain.PushNotification;

public record PushNotificationResponse(
    UUID id,
    String title,
    String body,
    NotificationType type,
    UUID referenceId,
    boolean read,
    LocalDateTime createdAt
) {
    public static PushNotificationResponse fromDomain(PushNotification notification) {
        return new PushNotificationResponse(
            notification.id(),
            notification.title(),
            notification.body(),
            notification.type(),
            notification.referenceId(),
            notification.readAt() != null,
            notification.createdAt()
        );
    }
}