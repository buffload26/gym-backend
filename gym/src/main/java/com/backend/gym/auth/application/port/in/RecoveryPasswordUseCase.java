package com.backend.gym.auth.application.port.in;

public interface RecoveryPasswordUseCase {
    void execute(RecoveryPasswordCommand command);

    record RecoveryPasswordCommand(String email) {}
}
