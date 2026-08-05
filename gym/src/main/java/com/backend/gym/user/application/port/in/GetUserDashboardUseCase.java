package com.backend.gym.user.application.port.in;

import java.util.UUID;

import com.backend.gym.user.domain.UserDashboard;

public interface GetUserDashboardUseCase {
    UserDashboard execute(UUID userId);
}