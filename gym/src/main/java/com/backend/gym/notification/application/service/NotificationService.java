package com.backend.gym.notification.application.service;

import org.springframework.stereotype.Service;

import com.backend.gym.notification.application.port.in.SendEmailUseCase;
import com.backend.gym.notification.application.port.out.EmailPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService implements SendEmailUseCase {

    private final EmailPort emailPort;

    @Override
    public void execute(SendEmailCommand command) {
        emailPort.send(command.to(), command.subject(), command.body());
    }
}
