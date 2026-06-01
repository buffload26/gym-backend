package com.backend.gym.user.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.gym.shared.exception.user.EmailAlreadyInUseException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.user.application.port.in.CreateUserUseCase;
import com.backend.gym.user.application.port.in.DeleteUserUseCase;
import com.backend.gym.user.application.port.in.GetUserUseCase;
import com.backend.gym.user.application.port.in.UpdateUserUseCase;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements
        CreateUserUseCase,
        GetUserUseCase,
        UpdateUserUseCase,
        DeleteUserUseCase {

    private final UserRepositoryPort repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User execute(CreateUserCommand command) {
        if (repository.existsByEmail(command.email())) {
            throw new EmailAlreadyInUseException(command.email());
        }

        String hashed = passwordEncoder.encode(command.password());

        User user = new User(
            null,
            command.name(),
            command.email(),
            hashed,
            null,
            null,
            false,
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        return repository.save(user);
    }

    @Override
    public User findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User findByEmail(String email) {
        return repository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(email));
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public User execute(UpdateUserCommand command) {
        User existing = findById(command.id());
        User updated = new User(
            existing.id(),
            command.name(),
            command.email(),
            existing.passwordHash(),
            existing.verificationCode(),
            existing.verificationCodeExpiresAt(),
            existing.isVerified(),
            existing.createdAt(),
            LocalDateTime.now()
        );
        return repository.save(updated);
    }

    @Override
    public void execute(UUID id) {
        findById(id);
        repository.deleteById(id);
    }
}
