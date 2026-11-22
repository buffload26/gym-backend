package com.backend.gym.notification.infrastructure.firebase;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.backend.gym.notification.application.port.out.AsyncPushNotificationPort;
import com.backend.gym.notification.application.port.out.PushNotificationPort;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AsyncFirebasePushNotificationAdapter implements AsyncPushNotificationPort {

    private final PushNotificationPort pushNotificationPort;

    @Async
    @Override
    public void sendAsync(List<String> tokens, String title, String body) {
        tokens.forEach(token ->
            pushNotificationPort.send(token, title, body)
        );
    }
}