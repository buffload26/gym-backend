package com.backend.gym.auth.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.gym.auth.application.port.in.LoginUseCase;
import com.backend.gym.auth.application.port.in.RefreshTokenUseCase;
import com.backend.gym.auth.application.port.out.JwtPort;
import com.backend.gym.auth.domain.TokenPair;
import com.backend.gym.shared.exception.auth.InvalidCredentialsException;
import com.backend.gym.shared.exception.auth.InvalidTokenException;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements LoginUseCase, RefreshTokenUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtPort jwtPort;

    @Override
    public TokenPair execute(LoginCommand command) {
        User user = userRepository.findByEmail(command.email())
            .orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(command.password(), user.passwordHash())) {
            throw new InvalidCredentialsException();
        }

        return new TokenPair(
            jwtPort.generateAccessToken(user.email()),
            jwtPort.generateRefreshToken(user.email())
        );
    }

    @Override
    public TokenPair execute(String refreshToken) {
        if (!jwtPort.isTokenValid(refreshToken)) {
            throw new InvalidTokenException();
        }

        if (!"refresh".equals(jwtPort.extractTokenType(refreshToken))) {
            throw new InvalidTokenException();
        }

        var email = jwtPort.extractEmail(refreshToken);

        return new TokenPair(
            jwtPort.generateAccessToken(email),
            jwtPort.generateRefreshToken(email)
        );
    }
}