package com.backend.gym.notification.application.port.out;

import java.util.List;

public interface AsyncPushNotificationPort {
    void sendAsync(List<String> tokens, String title, String body);
}