package com.backend.gym.user.infrastructure.web;

import com.backend.gym.user.application.port.in.CreateUserUseCase;
import com.backend.gym.user.application.port.in.CreateUserUseCase.CreateUserCommand;
import com.backend.gym.user.application.port.in.DeleteUserUseCase;
import com.backend.gym.user.application.port.in.GetUserUseCase;
import com.backend.gym.user.application.port.in.UpdateUserUseCase;
import com.backend.gym.user.application.port.in.UpdateUserUseCase.UpdateUserCommand;
import com.backend.gym.user.domain.User;
import com.backend.gym.user.infrastructure.web.dto.CreateUserRequest;
import com.backend.gym.user.infrastructure.web.dto.UpdateUserRequest;
import com.backend.gym.user.infrastructure.web.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    @PostMapping
    @Operation(summary = "Create user", description = "Creates a new user in the system")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User created successfully"),
        @ApiResponse(responseCode = "409", description = "Email already in use")
    })
    public ResponseEntity<UserResponse> create(@RequestBody CreateUserRequest request) {
        CreateUserCommand command = new CreateUserCommand(
            request.name(),
            request.email(),
            request.password()
        );
        User user = createUserUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromDomain(user));
    }

    @GetMapping
    @Operation(summary = "List users", description = "Returns a list of all users")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<Page<UserResponse>> findAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "name") String sort,
        @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageRequest.of(
            page, size,
            direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
            sort
        );

        Page<UserResponse> users = getUserUseCase.findAll(pageable)
            .map(UserResponse::fromDomain);

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Returns a single user by their ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        User user = getUserUseCase.findById(id);
        return ResponseEntity.ok(UserResponse.fromDomain(user));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Updates an existing user's data")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResponse> update(@PathVariable UUID id,
                                               @RequestBody UpdateUserRequest request) {
        UpdateUserCommand command = new UpdateUserCommand(id, request.name(), request.email());
        User user = updateUserUseCase.execute(command);
        return ResponseEntity.ok(UserResponse.fromDomain(user));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Permanently removes a user from the system")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUserUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
