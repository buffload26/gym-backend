package com.backend.gym.notification.infrastructure.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.backend.gym.notification.application.port.out.EmailPort;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;

@Component
public class ResendEmailAdapter implements EmailPort {

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from-email}")
    private String fromEmail;

    @Override
    public void send(String to, String subject, String body) {
        try {
            Resend resend = new Resend(apiKey);
            CreateEmailOptions request = CreateEmailOptions.builder()
                .from(fromEmail)
                .to(to)
                .subject(subject)
                .text(body)
                .build();
            resend.emails().send(request);
        } catch (ResendException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}