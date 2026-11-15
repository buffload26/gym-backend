package com.backend.gym.notification.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDeviceTokenJpaRepository
        extends JpaRepository<UserDeviceTokenEntity, UUID> {

    Optional<UserDeviceTokenEntity> findByToken(String token);
    List<UserDeviceTokenEntity> findAllByUserId(UUID userId);
    void deleteByUserIdAndToken(UUID userId, String token);
}