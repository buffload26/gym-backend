package com.backend.gym.shared.exception.competition;

import java.util.UUID;

import com.backend.gym.shared.exception.domain.NotFoundException;

public class CompetitionNotFoundException extends NotFoundException {

    public CompetitionNotFoundException(UUID id) {
        super("Competition not found with id: " + id);
    }
}