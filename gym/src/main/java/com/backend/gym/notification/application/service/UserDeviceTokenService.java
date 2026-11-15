package com.backend.gym.notification.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.gym.notification.application.port.in.RegisterDeviceTokenUseCase;
import com.backend.gym.notification.application.port.in.RemoveDeviceTokenUseCase;
import com.backend.gym.notification.application.port.out.UserDeviceTokenRepositoryPort;
import com.backend.gym.notification.domain.UserDeviceToken;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDeviceTokenService implements
        RegisterDeviceTokenUseCase,
        RemoveDeviceTokenUseCase {

    private final UserDeviceTokenRepositoryPort repository;
    private final UserRepositoryPort userRepository;

    @Override
    @Transactional
    public UserDeviceToken execute(RegisterDeviceTokenCommand command) {
        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

        // se o token já existe, atualiza o usuário dono dele
        return repository.findByToken(command.token())
            .map(existing -> {
                UserDeviceToken updated = new UserDeviceToken(
                    existing.id(),
                    user,
                    existing.token(),
                    command.deviceType(),
                    existing.createdAt(),
                    LocalDateTime.now()
                );
                return repository.save(updated);
            })
            .orElseGet(() -> {
                UserDeviceToken newToken = new UserDeviceToken(
                    null,
                    user,
                    command.token(),
                    command.deviceType(),
                    LocalDateTime.now(),
                    LocalDateTime.now()
                );
                return repository.save(newToken);
            });
    }

    @Override
    @Transactional
    public void execute(UUID userId, String token) {
        repository.deleteByUserIdAndToken(userId, token);
    }
}