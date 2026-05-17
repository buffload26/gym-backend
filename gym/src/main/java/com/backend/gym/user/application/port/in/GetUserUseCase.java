package com.backend.gym.user.application.port.in;

import java.util.List;
import java.util.UUID;

import com.backend.gym.user.domain.User;

public interface GetUserUseCase {
    User findById(UUID id);
    User findByEmail(String email);
    List<User> findAll();
}