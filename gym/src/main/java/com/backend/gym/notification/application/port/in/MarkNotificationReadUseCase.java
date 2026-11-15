package com.backend.gym.notification.application.port.in;

import java.util.UUID;

import com.backend.gym.notification.domain.PushNotification;

public interface MarkNotificationReadUseCase {
    PushNotification execute(UUID notificationId);
    void executeAll(UUID userId);
}