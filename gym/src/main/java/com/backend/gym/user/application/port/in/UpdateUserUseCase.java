package com.backend.gym.user.application.port.in;

import java.util.UUID;

import com.backend.gym.user.domain.User;

public interface UpdateUserUseCase {
    User execute(UpdateUserCommand command);

    record UpdateUserCommand(
        UUID id,
        String name,
        String email
    ) {}
}
