package com.backend.gym.auth.application.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.backend.gym.auth.application.port.in.LoginUseCase;
import com.backend.gym.auth.application.port.in.RecoveryPasswordUseCase;
import com.backend.gym.auth.application.port.in.RefreshTokenUseCase;
import com.backend.gym.auth.application.port.in.SendVerificationCodeUseCase;
import com.backend.gym.auth.application.port.in.UpdatePasswordUseCase;
import com.backend.gym.auth.application.port.in.ValidateVerificationCodeUseCase;
import com.backend.gym.auth.application.port.out.JwtPort;
import com.backend.gym.auth.domain.AuthResult;
import com.backend.gym.auth.domain.TokenPair;
import com.backend.gym.notification.application.port.in.SendEmailUseCase;
import com.backend.gym.notification.application.port.in.SendEmailUseCase.SendEmailCommand;
import com.backend.gym.shared.exception.auth.InvalidCredentialsException;
import com.backend.gym.shared.exception.auth.InvalidTokenException;
import com.backend.gym.shared.exception.auth.InvalidVerificationCodeException;
import com.backend.gym.shared.exception.auth.VerificationCodeExpiredException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.shared.exception.user.UserNotVerifiedException;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService implements
        LoginUseCase,
        RefreshTokenUseCase,
        SendVerificationCodeUseCase,
        ValidateVerificationCodeUseCase,
        RecoveryPasswordUseCase,
        UpdatePasswordUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtPort jwtPort;
    private final SendEmailUseCase sendEmailUseCase;

    @Override
    public AuthResult execute(LoginCommand command) {
        User user = userRepository.findByEmail(command.email())
            .orElseThrow(() -> new InvalidCredentialsException());

        if (!passwordEncoder.matches(command.password(), user.passwordHash())) {
            throw new InvalidCredentialsException();
        }

        if (!user.isVerified()) {
            throw new UserNotVerifiedException();
        }

        return new AuthResult(
            new TokenPair(
                jwtPort.generateAccessToken(user.email()),
                jwtPort.generateRefreshToken(user.email())
            ),
            user
        );
    }

    @Override
    public AuthResult execute(String refreshToken) {
        if (!jwtPort.isTokenValid(refreshToken)) {
            throw new InvalidTokenException();
        }

        if (!"refresh".equals(jwtPort.extractTokenType(refreshToken))) {
            throw new InvalidTokenException();
        }

        String email = jwtPort.extractEmail(refreshToken);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        return new AuthResult(
            new TokenPair(
                jwtPort.generateAccessToken(email),
                jwtPort.generateRefreshToken(email)
            ),
            user
        );
    }

    @Override
    public void execute(SendVerificationCodeCommand command) {
        User user = userRepository.findByEmail(command.email())
            .orElseThrow(() -> new UserNotFoundException(command.email()));

        String code = generateCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        User updated = new User(
            user.id(), user.name(), user.email(), user.passwordHash(),
            code, expiresAt, false,
            user.createdAt(), LocalDateTime.now()
        );
        userRepository.save(updated);

        sendEmailUseCase.execute(new SendEmailCommand(
            user.email(),
            "Email verification",
            "Your verification code is: " + code + "\nExpires in 15 minutes."
        ));
    }

    @Override
    public void execute(ValidateVerificationCodeCommand command) {
        User user = userRepository.findByEmail(command.email())
            .orElseThrow(() -> new UserNotFoundException(command.email()));

        if (user.verificationCode() == null) {
            throw new InvalidVerificationCodeException();
        }
        
        if (!command.code().equals(user.verificationCode())) {
            throw new InvalidVerificationCodeException();
        }

        if (LocalDateTime.now().isAfter(user.verificationCodeExpiresAt())) {
            throw new VerificationCodeExpiredException();
        }

        User verified = new User(
            user.id(), user.name(), user.email(), user.passwordHash(),
            user.verificationCode(), user.verificationCodeExpiresAt(), true,
            user.createdAt(), LocalDateTime.now()
        );
        userRepository.save(verified);
    }

    @Override
    public void execute(RecoveryPasswordCommand command) {
        User user = userRepository.findByEmail(command.email())
            .orElseThrow(() -> new UserNotFoundException(command.email()));

        String code = generateCode();
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(15);

        User updated = new User(
            user.id(), user.name(), user.email(), user.passwordHash(),
            code, expiresAt, user.isVerified(),
            user.createdAt(), LocalDateTime.now()
        );
        userRepository.save(updated);

        sendEmailUseCase.execute(new SendEmailCommand(
            user.email(),
            "Password recovery",
            "Your recovery code is: " + code + "\nExpires in 15 minutes."
        ));
    }

    @Override
    public void execute(UpdatePasswordCommand command) {
        User user = userRepository.findByEmail(command.email())
            .orElseThrow(() -> new UserNotFoundException(command.email()));

        if (!user.isVerified()) {
            throw new UserNotVerifiedException();
        }

        if (!command.code().equals(user.verificationCode())) {
            throw new InvalidVerificationCodeException();
        }

        if (LocalDateTime.now().isAfter(user.verificationCodeExpiresAt())) {
            throw new VerificationCodeExpiredException();
        }

        User updated = new User(
            user.id(), user.name(), user.email(),
            passwordEncoder.encode(command.newPassword()),
            null, null, user.isVerified(),
            user.createdAt(), LocalDateTime.now()
        );
        userRepository.save(updated);
    }

    private String generateCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }
}