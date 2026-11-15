package com.backend.gym.notification.application.port.in;

import com.backend.gym.notification.domain.DeviceType;
import com.backend.gym.notification.domain.UserDeviceToken;

public interface RegisterDeviceTokenUseCase {
    record RegisterDeviceTokenCommand(
        java.util.UUID userId,
        String token,
        DeviceType deviceType
    ) {}

    UserDeviceToken execute(RegisterDeviceTokenCommand command);
}