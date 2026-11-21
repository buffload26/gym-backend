package com.backend.gym.shared.exception.ranking;

import java.util.UUID;

import com.backend.gym.shared.exception.domain.NotFoundException;

public class SubmissionNotFoundException extends NotFoundException {
    public SubmissionNotFoundException(UUID id) {
        super("Submission not found with id: " + id);
    }
}