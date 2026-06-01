package com.backend.gym.user.application.port.in;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.gym.user.domain.User;

public interface GetUserUseCase {
    User findById(UUID id);
    User findByEmail(String email);
    Page<User> findAll(Pageable pageable); 
}