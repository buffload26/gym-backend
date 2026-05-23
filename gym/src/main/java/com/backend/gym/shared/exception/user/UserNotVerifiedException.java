package com.backend.gym.shared.exception.user;

import com.backend.gym.shared.exception.domain.BusinessException;

public class UserNotVerifiedException extends BusinessException {
    public UserNotVerifiedException() {
        super("User is not verified. Please verify your email first.");
    }
}
