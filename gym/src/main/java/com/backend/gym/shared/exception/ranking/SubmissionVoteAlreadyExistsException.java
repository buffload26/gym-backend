package com.backend.gym.shared.exception.ranking;

import com.backend.gym.shared.exception.domain.AlreadyExistsException;

public class SubmissionVoteAlreadyExistsException extends AlreadyExistsException {
    public SubmissionVoteAlreadyExistsException() {
        super("User already voted on this submission");
    }
}