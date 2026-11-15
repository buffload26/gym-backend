package com.backend.gym.notification.application.port.in;

import java.util.UUID;

import com.backend.gym.notification.domain.NotificationType;
import com.backend.gym.notification.domain.PushNotification;

public interface SendPushNotificationUseCase {
    record SendPushNotificationCommand(
        UUID userId,
        String title,
        String body,
        NotificationType type,
        UUID referenceId
    ) {}

    PushNotification execute(SendPushNotificationCommand command);
}