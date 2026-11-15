package com.backend.gym.notification.infrastructure.firebase;

import org.springframework.stereotype.Component;

import com.backend.gym.notification.application.port.out.PushNotificationPort;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FirebasePushNotificationAdapter implements PushNotificationPort {

    @Override
    public void send(String token, String title, String body) {
        try {
            Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                    .setTitle(title)
                    .setBody(body)
                    .build())
                .build();

            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Push notification sent successfully: {}", response);
        } catch (Exception e) {
            log.error("Failed to send push notification to token {}: {}", token, e.getMessage());
        }
    }
}