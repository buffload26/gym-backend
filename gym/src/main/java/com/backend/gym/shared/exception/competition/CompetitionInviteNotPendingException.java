package com.backend.gym.shared.exception.competition;

import com.backend.gym.shared.exception.domain.BusinessException;

public class CompetitionInviteNotPendingException extends BusinessException {
    public CompetitionInviteNotPendingException() {
        super("Competition invite is not pending");
    }
}