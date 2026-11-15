package com.backend.gym.notification.infrastructure.web;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.gym.notification.application.port.in.RegisterDeviceTokenUseCase;
import com.backend.gym.notification.application.port.in.RegisterDeviceTokenUseCase.RegisterDeviceTokenCommand;
import com.backend.gym.notification.application.port.in.RemoveDeviceTokenUseCase;
import com.backend.gym.notification.infrastructure.web.dto.RegisterDeviceTokenRequest;
import com.backend.gym.notification.infrastructure.web.dto.UserDeviceTokenResponse;
import com.backend.gym.notification.domain.UserDeviceToken;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users/{userId}/devices")
@RequiredArgsConstructor
@Tag(name = "Devices", description = "User device token management")
public class UserDeviceTokenController {

    private final RegisterDeviceTokenUseCase registerDeviceTokenUseCase;
    private final RemoveDeviceTokenUseCase removeDeviceTokenUseCase;

    @PostMapping
    @Operation(summary = "Register device token", description = "Registers or updates a device token for push notifications")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Token registered successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDeviceTokenResponse> register(
            @PathVariable UUID userId,
            @RequestBody RegisterDeviceTokenRequest request) {

        RegisterDeviceTokenCommand command = new RegisterDeviceTokenCommand(
            userId,
            request.token(),
            request.deviceType()
        );

        UserDeviceToken deviceToken = registerDeviceTokenUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(UserDeviceTokenResponse.fromDomain(deviceToken));
    }

    @DeleteMapping("/{token}")
    @Operation(summary = "Remove device token", description = "Removes a device token on logout")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Token removed successfully")
    })
    public ResponseEntity<Void> remove(
            @PathVariable UUID userId,
            @PathVariable String token) {

        removeDeviceTokenUseCase.execute(userId, token);

        return ResponseEntity.noContent().build();
    }
}