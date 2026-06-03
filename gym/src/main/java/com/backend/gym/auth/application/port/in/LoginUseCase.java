package com.backend.gym.auth.application.port.in;

import com.backend.gym.auth.domain.AuthResult;

public interface LoginUseCase {
    AuthResult execute(LoginCommand command);

    record LoginCommand(String email, String password) {}
}