package com.backend.gym.user.application.port.in;

import com.backend.gym.user.domain.User;

public interface CreateUserUseCase {
    User execute(CreateUserCommand command);

    record CreateUserCommand(
        String name,
        String email,
        String password
    ) {}
}