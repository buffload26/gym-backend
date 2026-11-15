package com.backend.gym.shared.exception.competition;

public class CompetitionInviteNotPendingException extends RuntimeException {
    public CompetitionInviteNotPendingException() {
        super("Competition invite is not pending");
    }
}