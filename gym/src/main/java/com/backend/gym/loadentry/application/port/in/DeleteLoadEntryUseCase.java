package com.backend.gym.loadentry.application.port.in;

import java.util.UUID;

public interface DeleteLoadEntryUseCase {
    void execute(UUID id);
}