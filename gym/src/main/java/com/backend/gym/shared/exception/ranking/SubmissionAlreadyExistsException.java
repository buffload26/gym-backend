package com.backend.gym.shared.exception.ranking;

import com.backend.gym.shared.exception.domain.AlreadyExistsException;

public class SubmissionAlreadyExistsException extends AlreadyExistsException {
    public SubmissionAlreadyExistsException() {
        super("User already has a submission for this competition");
    }
}