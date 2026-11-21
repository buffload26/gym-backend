package com.backend.gym.shared.exception.ranking;

import com.backend.gym.shared.exception.domain.BusinessException;

public class SubmissionSelfVoteException extends BusinessException {
    public SubmissionSelfVoteException() {
        super("User cannot vote on their own submission");
    }
}