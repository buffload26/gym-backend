package com.backend.gym.shared.exception.competition;

import java.util.UUID;

public class CompetitionNotFoundException extends RuntimeException {

    public CompetitionNotFoundException(UUID id) {
        super("Competition not found with id: " + id);
    }
}