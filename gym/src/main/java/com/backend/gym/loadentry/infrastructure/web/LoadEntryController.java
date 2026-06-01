package com.backend.gym.loadentry.infrastructure.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.gym.loadentry.application.port.in.CreateLoadEntryUseCase;
import com.backend.gym.loadentry.application.port.in.CreateLoadEntryUseCase.CreateLoadEntryCommand;
import com.backend.gym.loadentry.application.port.in.DeleteLoadEntryUseCase;
import com.backend.gym.loadentry.application.port.in.GetLoadEntryUseCase;
import com.backend.gym.loadentry.domain.LoadEntry;
import com.backend.gym.loadentry.infrastructure.web.dto.CreateLoadEntryRequest;
import com.backend.gym.loadentry.infrastructure.web.dto.LoadEntryResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/load-entries")
@RequiredArgsConstructor
@Tag(name = "Load Entries", description = "Load entry management")
public class LoadEntryController {

    private final CreateLoadEntryUseCase registerLoadEntryUseCase;
    private final GetLoadEntryUseCase getLoadEntryUseCase;
    private final DeleteLoadEntryUseCase deleteLoadEntryUseCase;

    @PostMapping
    @Operation(summary = "Create load entry", description = "Creates a new load entry for an exercise")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Load entry created successfully"),
        @ApiResponse(responseCode = "404", description = "User, exercise or workout not found")
    })
    public ResponseEntity<LoadEntryResponse> register(@RequestBody CreateLoadEntryRequest request) {
        CreateLoadEntryCommand command = new CreateLoadEntryCommand(
            request.userId(), request.exerciseId(), request.workoutId(),
            request.performedAt(), request.loadKg(), request.sets(),
            request.reps(), request.notes()
        );
        LoadEntry loadEntry = registerLoadEntryUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(LoadEntryResponse.fromDomain(loadEntry));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get load entry by ID", description = "Returns a single load entry by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Load entry found"),
        @ApiResponse(responseCode = "404", description = "Load entry not found")
    })
    public ResponseEntity<LoadEntryResponse> findById(@PathVariable UUID id) {
        LoadEntry loadEntry = getLoadEntryUseCase.findById(id);
        return ResponseEntity.ok(LoadEntryResponse.fromDomain(loadEntry));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List load entries by user", description = "Returns all load entries for a specific user")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<List<LoadEntryResponse>> findAllByUser(@PathVariable UUID userId) {
        List<LoadEntryResponse> entries = getLoadEntryUseCase.findAllByUser(userId).stream()
            .map(LoadEntryResponse::fromDomain)
            .toList();
        return ResponseEntity.ok(entries);
    }

    @GetMapping("/exercise/{exerciseId}")
    @Operation(summary = "List load entries by exercise", description = "Returns all load entries for a specific exercise")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<List<LoadEntryResponse>> findAllByExercise(@PathVariable UUID exerciseId) {
        List<LoadEntryResponse> entries = getLoadEntryUseCase.findAllByExercise(exerciseId).stream()
            .map(LoadEntryResponse::fromDomain)
            .toList();
        return ResponseEntity.ok(entries);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete load entry", description = "Permanently removes a load entry")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Load entry deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Load entry not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteLoadEntryUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
