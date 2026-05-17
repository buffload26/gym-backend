package com.backend.gym.shared.exception.user;

import java.util.UUID;

import com.backend.gym.shared.exception.domain.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(UUID id) {
        super("User not found with id: " + id);
    }

    public UserNotFoundException(String email) {
        super("User not found with email: " + email);
    }
}