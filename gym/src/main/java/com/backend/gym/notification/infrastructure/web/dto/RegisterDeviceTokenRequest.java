package com.backend.gym.notification.infrastructure.web.dto;

import com.backend.gym.notification.domain.DeviceType;

public record RegisterDeviceTokenRequest(
    String token,
    DeviceType deviceType
) {}