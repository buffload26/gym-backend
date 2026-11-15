package com.backend.gym.notification.application.port.out;

public interface PushNotificationPort {
    void send(String token, String title, String body);
}
