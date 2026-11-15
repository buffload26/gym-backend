package com.backend.gym.notification.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.backend.gym.notification.application.port.out.UserDeviceTokenRepositoryPort;
import com.backend.gym.notification.domain.UserDeviceToken;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDeviceTokenRepositoryAdapter implements UserDeviceTokenRepositoryPort {

    private final UserDeviceTokenJpaRepository jpaRepository;

    @Override
    public UserDeviceToken save(UserDeviceToken deviceToken) {
        return jpaRepository.save(
            UserDeviceTokenEntity.fromDomain(deviceToken)
        ).toDomain();
    }

    @Override
    public Optional<UserDeviceToken> findByToken(String token) {
        return jpaRepository.findByToken(token)
            .map(UserDeviceTokenEntity::toDomain);
    }

    @Override
    public List<UserDeviceToken> findAllByUserId(UUID userId) {
        return jpaRepository.findAllByUserId(userId).stream()
            .map(UserDeviceTokenEntity::toDomain)
            .toList();
    }

    @Override
    public void deleteByUserIdAndToken(UUID userId, String token) {
        jpaRepository.deleteByUserIdAndToken(userId, token);
    }
}