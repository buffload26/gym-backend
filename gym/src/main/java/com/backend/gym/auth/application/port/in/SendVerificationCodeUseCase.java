package com.backend.gym.auth.application.port.in;

public interface SendVerificationCodeUseCase {
    void execute(SendVerificationCodeCommand command);

    record SendVerificationCodeCommand(String email) {}
}
