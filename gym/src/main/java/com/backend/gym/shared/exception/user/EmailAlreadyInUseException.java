package com.backend.gym.shared.exception.user;

import com.backend.gym.shared.exception.domain.AlreadyExistsException;

public class EmailAlreadyInUseException extends AlreadyExistsException {
    public EmailAlreadyInUseException(String email) {
        super("Email already in use: " + email);
    }
}
