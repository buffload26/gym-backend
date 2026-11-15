package com.backend.gym.notification.application.port.out;

import com.backend.gym.notification.domain.PushNotification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface PushNotificationRepositoryPort {
    PushNotification save(PushNotification notification);
    Optional<PushNotification> findById(UUID id);
    Page<PushNotification> findAllByUserId(UUID userId, Pageable pageable);
    long countUnreadByUserId(UUID userId);
    void markAllAsReadByUserId(UUID userId);
}