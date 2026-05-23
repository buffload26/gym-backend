package com.backend.gym.auth.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.gym.auth.application.port.in.LoginUseCase;
import com.backend.gym.auth.application.port.in.LoginUseCase.LoginCommand;
import com.backend.gym.auth.application.port.in.RecoveryPasswordUseCase;
import com.backend.gym.auth.application.port.in.RecoveryPasswordUseCase.RecoveryPasswordCommand;
import com.backend.gym.auth.application.port.in.RefreshTokenUseCase;
import com.backend.gym.auth.application.port.in.SendVerificationCodeUseCase;
import com.backend.gym.auth.application.port.in.SendVerificationCodeUseCase.SendVerificationCodeCommand;
import com.backend.gym.auth.application.port.in.UpdatePasswordUseCase;
import com.backend.gym.auth.application.port.in.UpdatePasswordUseCase.UpdatePasswordCommand;
import com.backend.gym.auth.application.port.in.ValidateVerificationCodeUseCase;
import com.backend.gym.auth.application.port.in.ValidateVerificationCodeUseCase.ValidateVerificationCodeCommand;
import com.backend.gym.auth.domain.TokenPair;
import com.backend.gym.auth.infrastructure.web.dto.LoginRequest;
import com.backend.gym.auth.infrastructure.web.dto.MessageResponse;
import com.backend.gym.auth.infrastructure.web.dto.RecoveryPasswordRequest;
import com.backend.gym.auth.infrastructure.web.dto.SendVerificationCodeRequest;
import com.backend.gym.auth.infrastructure.web.dto.TokenResponse;
import com.backend.gym.auth.infrastructure.web.dto.UpdatePasswordRequest;
import com.backend.gym.auth.infrastructure.web.dto.ValidateVerificationCodeRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Authentication management")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final SendVerificationCodeUseCase sendVerificationCodeUseCase;
    private final ValidateVerificationCodeUseCase validateVerificationCodeUseCase;
    private final RecoveryPasswordUseCase recoveryPasswordUseCase;
    private final UpdatePasswordUseCase updatePasswordUseCase;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns a token pair")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authenticated successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        LoginCommand command = new LoginCommand(request.email(), request.password());
        TokenPair tokenPair = loginUseCase.execute(command);
        return ResponseEntity.ok(TokenResponse.fromDomain(tokenPair));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Generates a new token pair from a refresh token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    public ResponseEntity<TokenResponse> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        TokenPair tokenPair = refreshTokenUseCase.execute(refreshToken);
        return ResponseEntity.ok(TokenResponse.fromDomain(tokenPair));
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<MessageResponse> sendVerificationCode(@RequestBody SendVerificationCodeRequest request) {
        sendVerificationCodeUseCase.execute(new SendVerificationCodeCommand(request.email()));
        return ResponseEntity.ok(new MessageResponse("Verification code sent to " + request.email()));
    }

    @PostMapping("/validate-verification-code")
    public ResponseEntity<MessageResponse> validateVerificationCode(@RequestBody ValidateVerificationCodeRequest request) {
        validateVerificationCodeUseCase.execute(new ValidateVerificationCodeCommand(request.email(), request.code()));
        return ResponseEntity.ok(new MessageResponse("Email verified successfully"));
    }

    @PostMapping("/recovery-password")
    public ResponseEntity<MessageResponse> recoveryPassword(@RequestBody RecoveryPasswordRequest request) {
        recoveryPasswordUseCase.execute(new RecoveryPasswordCommand(request.email()));
        return ResponseEntity.ok(new MessageResponse("Recovery code sent to " + request.email()));
    }

    @PatchMapping("/update-password")
    public ResponseEntity<MessageResponse> updatePassword(@RequestBody UpdatePasswordRequest request) {
        updatePasswordUseCase.execute(new UpdatePasswordCommand(
            request.email(), request.code(), request.newPassword()
        ));
        return ResponseEntity.ok(new MessageResponse("Password updated successfully"));
    }
}