package com.backend.gym.auth.infrastructure.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.gym.auth.application.port.in.LoginUseCase;
import com.backend.gym.auth.application.port.in.LoginUseCase.LoginCommand;
import com.backend.gym.auth.application.port.in.RefreshTokenUseCase;
import com.backend.gym.auth.infrastructure.web.dto.LoginRequest;
import com.backend.gym.auth.infrastructure.web.dto.TokenResponse;

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

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticates a user and returns a token pair")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Authenticated successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        var command = new LoginCommand(request.email(), request.password());
        var tokenPair = loginUseCase.execute(command);
        return ResponseEntity.ok(TokenResponse.fromDomain(tokenPair));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Generates a new token pair from a refresh token")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
        @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    public ResponseEntity<TokenResponse> refresh(@RequestHeader("Refresh-Token") String refreshToken) {
        var tokenPair = refreshTokenUseCase.execute(refreshToken);
        return ResponseEntity.ok(TokenResponse.fromDomain(tokenPair));
    }
}