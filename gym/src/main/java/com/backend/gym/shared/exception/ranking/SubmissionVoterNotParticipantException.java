package com.backend.gym.shared.exception.ranking;

import com.backend.gym.shared.exception.domain.BusinessException;

public class SubmissionVoterNotParticipantException extends BusinessException {
    public SubmissionVoterNotParticipantException() {
        super("Only competition participants can vote on submissions");
    }
}