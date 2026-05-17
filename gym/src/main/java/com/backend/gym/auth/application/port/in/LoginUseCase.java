package com.backend.gym.auth.application.port.in;

import com.backend.gym.auth.domain.TokenPair;

public interface LoginUseCase {
    TokenPair execute(LoginCommand command);

    record LoginCommand(String email, String password) {}
}