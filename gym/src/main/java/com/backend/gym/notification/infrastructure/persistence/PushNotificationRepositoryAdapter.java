package com.backend.gym.notification.infrastructure.persistence;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.backend.gym.notification.application.port.out.PushNotificationRepositoryPort;
import com.backend.gym.notification.domain.PushNotification;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PushNotificationRepositoryAdapter implements PushNotificationRepositoryPort {

    private final PushNotificationJpaRepository jpaRepository;

    @Override
    public PushNotification save(PushNotification notification) {
        return jpaRepository.save(
            PushNotificationEntity.fromDomain(notification)
        ).toDomain();
    }

    @Override
    public Optional<PushNotification> findById(UUID id) {
        return jpaRepository.findById(id)
            .map(PushNotificationEntity::toDomain);
    }

    @Override
    public Page<PushNotification> findAllByUserId(UUID userId, Pageable pageable) {
        return jpaRepository.findAllByUserId(userId, pageable)
            .map(PushNotificationEntity::toDomain);
    }

    @Override
    public long countUnreadByUserId(UUID userId) {
        return jpaRepository.countUnreadByUserId(userId);
    }

    @Override
    public void markAllAsReadByUserId(UUID userId) {
        jpaRepository.markAllAsReadByUserId(userId);
    }
}