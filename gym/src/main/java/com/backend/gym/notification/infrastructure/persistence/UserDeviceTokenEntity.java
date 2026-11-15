package com.backend.gym.notification.infrastructure.persistence;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.notification.domain.DeviceType;
import com.backend.gym.notification.domain.UserDeviceToken;
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
@Table(name = "user_device_tokens")
public class UserDeviceTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, unique = true, columnDefinition = "TEXT")
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false, length = 20)
    private DeviceType deviceType;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public UserDeviceToken toDomain() {
        return new UserDeviceToken(
            id,
            user != null ? user.toDomain() : null,
            token,
            deviceType,
            createdAt,
            updatedAt
        );
    }

    public static UserDeviceTokenEntity fromDomain(UserDeviceToken deviceToken) {
        return UserDeviceTokenEntity.builder()
            .id(deviceToken.id())
            .user(
                deviceToken.user() != null
                    ? UserEntity.fromDomain(deviceToken.user())
                    : null
            )
            .token(deviceToken.token())
            .deviceType(deviceToken.deviceType())
            .createdAt(deviceToken.createdAt())
            .updatedAt(deviceToken.updatedAt())
            .build();
    }
}