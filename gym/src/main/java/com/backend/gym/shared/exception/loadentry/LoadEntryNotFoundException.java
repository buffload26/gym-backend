package com.backend.gym.shared.exception.loadentry;

import java.util.UUID;

import com.backend.gym.shared.exception.domain.NotFoundException;

public class LoadEntryNotFoundException extends NotFoundException {
    public LoadEntryNotFoundException(UUID id) {
        super("Load entry not found with id: " + id);
    }
}
