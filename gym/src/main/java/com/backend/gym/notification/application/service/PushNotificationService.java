package com.backend.gym.notification.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.gym.notification.application.port.in.GetNotificationsUseCase;
import com.backend.gym.notification.application.port.in.MarkNotificationReadUseCase;
import com.backend.gym.notification.application.port.in.SendPushNotificationUseCase;
import com.backend.gym.notification.application.port.out.AsyncPushNotificationPort;
import com.backend.gym.notification.application.port.out.PushNotificationPort;
import com.backend.gym.notification.application.port.out.PushNotificationRepositoryPort;
import com.backend.gym.notification.application.port.out.UserDeviceTokenRepositoryPort;
import com.backend.gym.notification.domain.PushNotification;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PushNotificationService implements
        SendPushNotificationUseCase,
        GetNotificationsUseCase,
        MarkNotificationReadUseCase {

    private final PushNotificationRepositoryPort notificationRepository;
    private final UserDeviceTokenRepositoryPort deviceTokenRepository;
    private final UserRepositoryPort userRepository;
    private final AsyncPushNotificationPort asyncPushNotificationPort;

    @Override
    @Transactional
    public PushNotification execute(SendPushNotificationCommand command) {
        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

        PushNotification notification = new PushNotification(
            null,
            user,
            command.title(),
            command.body(),
            command.type(),
            command.referenceId(),
            null,
            LocalDateTime.now()
        );

        PushNotification saved = notificationRepository.save(notification);

        List<String> tokens = deviceTokenRepository.findAllByUserId(command.userId())
            .stream()
            .map(dt -> dt.token())
            .toList();

        asyncPushNotificationPort.sendAsync(tokens, command.title(), command.body());

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PushNotification> findAllByUserId(UUID userId, Pageable pageable) {
        return notificationRepository.findAllByUserId(userId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadByUserId(UUID userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    @Override
    @Transactional
    public PushNotification execute(UUID notificationId) {
        PushNotification existing = notificationRepository.findById(notificationId)
            .orElseThrow(() -> new IllegalArgumentException("Notification not found"));

        if (existing.readAt() != null) {
            return existing;
        }

        PushNotification read = new PushNotification(
            existing.id(),
            existing.user(),
            existing.title(),
            existing.body(),
            existing.type(),
            existing.referenceId(),
            LocalDateTime.now(),
            existing.createdAt()
        );

        return notificationRepository.save(read);
    }

    @Override
    @Transactional
    public void executeAll(UUID userId) {
        notificationRepository.markAllAsReadByUserId(userId);
    }
}