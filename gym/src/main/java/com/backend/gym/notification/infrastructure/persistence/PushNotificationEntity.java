package com.backend.gym.notification.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.notification.domain.NotificationType;
import com.backend.gym.notification.domain.PushNotification;
import com.backend.gym.user.infrastructure.persistence.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "push_notifications")
public class PushNotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NotificationType type;

    @Column(name = "reference_id")
    private UUID referenceId;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public PushNotification toDomain() {
        return new PushNotification(
            id,
            user != null ? user.toDomain() : null,
            title,
            body,
            type,
            referenceId,
            readAt,
            createdAt
        );
    }

    public static PushNotificationEntity fromDomain(PushNotification notification) {
        return PushNotificationEntity.builder()
            .id(notification.id())
            .user(
                notification.user() != null
                    ? UserEntity.fromDomain(notification.user())
                    : null
            )
            .title(notification.title())
            .body(notification.body())
            .type(notification.type())
            .referenceId(notification.referenceId())
            .readAt(notification.readAt())
            .createdAt(notification.createdAt())
            .build();
    }
}