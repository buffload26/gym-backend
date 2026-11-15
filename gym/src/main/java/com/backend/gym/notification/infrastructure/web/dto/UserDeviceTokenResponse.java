package com.backend.gym.notification.infrastructure.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.notification.domain.DeviceType;
import com.backend.gym.notification.domain.UserDeviceToken;

public record UserDeviceTokenResponse(
    UUID id,
    String token,
    DeviceType deviceType,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserDeviceTokenResponse fromDomain(UserDeviceToken deviceToken) {
        return new UserDeviceTokenResponse(
            deviceToken.id(),
            deviceToken.token(),
            deviceToken.deviceType(),
            deviceToken.createdAt(),
            deviceToken.updatedAt()
        );
    }
}