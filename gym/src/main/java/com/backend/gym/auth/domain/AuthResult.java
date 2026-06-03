package com.backend.gym.auth.domain;

import com.backend.gym.user.domain.User;

public record AuthResult(TokenPair tokenPair, User user) {}

