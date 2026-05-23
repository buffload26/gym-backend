package com.backend.gym.auth.application.port.in;

public interface ValidateVerificationCodeUseCase {
    void execute(ValidateVerificationCodeCommand command);

    record ValidateVerificationCodeCommand(
        String email,
        String code
    ) {}
}