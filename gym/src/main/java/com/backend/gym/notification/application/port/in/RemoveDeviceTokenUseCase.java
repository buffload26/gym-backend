package com.backend.gym.notification.application.port.in;

import java.util.UUID;

public interface RemoveDeviceTokenUseCase {
    void execute(UUID userId, String token);
}