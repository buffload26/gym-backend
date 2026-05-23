package com.backend.gym.notification.application.port.in;

public interface SendEmailUseCase {
    void execute(SendEmailCommand command);

    record SendEmailCommand(
        String to,
        String subject,
        String body
    ) {}
}
