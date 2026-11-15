package com.backend.gym.notification.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.notification.domain.PushNotification;

import java.util.UUID;

public interface GetNotificationsUseCase {
    Page<PushNotification> findAllByUserId(UUID userId, Pageable pageable);
    long countUnreadByUserId(UUID userId);
}