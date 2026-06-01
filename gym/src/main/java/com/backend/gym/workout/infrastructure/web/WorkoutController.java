package com.backend.gym.workout.infrastructure.web;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.gym.workout.application.port.in.CreateWorkoutUseCase;
import com.backend.gym.workout.application.port.in.CreateWorkoutUseCase.CreateWorkoutCommand;
import com.backend.gym.workout.application.port.in.DeleteWorkoutUseCase;
import com.backend.gym.workout.application.port.in.GetWorkoutUseCase;
import com.backend.gym.workout.application.port.in.UpdateWorkoutUseCase;
import com.backend.gym.workout.application.port.in.UpdateWorkoutUseCase.UpdateWorkoutCommand;
import com.backend.gym.workout.domain.Workout;
import com.backend.gym.workout.infrastructure.web.dto.CreateWorkoutRequest;
import com.backend.gym.workout.infrastructure.web.dto.UpdateWorkoutRequest;
import com.backend.gym.workout.infrastructure.web.dto.WorkoutResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/workouts")
@RequiredArgsConstructor
@Tag(name = "Workouts", description = "Workout management")
public class WorkoutController {

    private final CreateWorkoutUseCase createWorkoutUseCase;
    private final GetWorkoutUseCase getWorkoutUseCase;
    private final UpdateWorkoutUseCase updateWorkoutUseCase;
    private final DeleteWorkoutUseCase deleteWorkoutUseCase;

    @PostMapping
    @Operation(summary = "Create workout", description = "Creates a new workout")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Workout created successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<WorkoutResponse> create(@RequestBody CreateWorkoutRequest request) {
        CreateWorkoutCommand command = new CreateWorkoutCommand(
        request.userId(), request.name(), request.description()
    );
        Workout workout = createWorkoutUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(WorkoutResponse.fromDomain(workout));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get workout by ID", description = "Returns a single workout by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Workout found"),
        @ApiResponse(responseCode = "404", description = "Workout not found")
    })
    public ResponseEntity<WorkoutResponse> findById(@PathVariable UUID id) {
        var workout = getWorkoutUseCase.findById(id);
        return ResponseEntity.ok(WorkoutResponse.fromDomain(workout));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List workouts by user", description = "Returns all workouts for a specific user")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<Page<WorkoutResponse>> findAllByUser(
        @PathVariable UUID userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdAt") String sort,
        @RequestParam(defaultValue = "desc") String direction) {

        Pageable pageable = PageRequest.of(
            page, size,
            direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
            sort
        );

        Page<WorkoutResponse> workouts = getWorkoutUseCase.findAllByUser(userId, pageable)
            .map(workout -> WorkoutResponse.fromDomain(workout));

        return ResponseEntity.ok(workouts);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update workout", description = "Updates an existing workout")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Workout updated successfully"),
        @ApiResponse(responseCode = "404", description = "Workout not found")
    })
    public ResponseEntity<WorkoutResponse> update(@PathVariable UUID id,
                                                  @RequestBody UpdateWorkoutRequest request) {
        UpdateWorkoutCommand command = new UpdateWorkoutCommand(id, request.name(), request.description());
        Workout workout = updateWorkoutUseCase.execute(command);
        return ResponseEntity.ok(WorkoutResponse.fromDomain(workout));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete workout", description = "Permanently removes a workout")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Workout deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Workout not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteWorkoutUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
