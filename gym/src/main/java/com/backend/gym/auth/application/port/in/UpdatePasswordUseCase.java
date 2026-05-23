package com.backend.gym.auth.application.port.in;

public interface UpdatePasswordUseCase {
    void execute(UpdatePasswordCommand command);

    record UpdatePasswordCommand(
        String email,
        String code,
        String newPassword
    ) {}
}
