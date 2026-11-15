package com.backend.gym.notification.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.backend.gym.notification.domain.UserDeviceToken;

public interface UserDeviceTokenRepositoryPort {
    UserDeviceToken save(UserDeviceToken deviceToken);
    Optional<UserDeviceToken> findByToken(String token);
    List<UserDeviceToken> findAllByUserId(UUID userId);
    void deleteByUserIdAndToken(UUID userId, String token);
}