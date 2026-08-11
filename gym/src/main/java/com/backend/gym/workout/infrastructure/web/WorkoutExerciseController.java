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

import com.backend.gym.workout.application.port.in.CreateExerciseToWorkoutUseCase;
import com.backend.gym.workout.application.port.in.CreateExerciseToWorkoutUseCase.CreateExerciseToWorkoutCommand;
import com.backend.gym.workout.application.port.in.DeleteExerciseFromWorkoutUseCase;
import com.backend.gym.workout.application.port.in.GetWorkoutExerciseUseCase;
import com.backend.gym.workout.application.port.in.UpdateWorkoutExerciseUseCase;
import com.backend.gym.workout.application.port.in.UpdateWorkoutExerciseUseCase.UpdateWorkoutExerciseCommand;
import com.backend.gym.workout.domain.WorkoutExercise;
import com.backend.gym.workout.infrastructure.web.dto.CreateExerciseToWorkoutRequest;
import com.backend.gym.workout.infrastructure.web.dto.UpdateWorkoutExerciseRequest;
import com.backend.gym.workout.infrastructure.web.dto.WorkoutExerciseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/workout-exercises")
@RequiredArgsConstructor
@Tag(name = "Workout Exercises", description = "Workout exercise management")
public class WorkoutExerciseController {

    private final CreateExerciseToWorkoutUseCase createExerciseToWorkoutUseCase;
    private final GetWorkoutExerciseUseCase getWorkoutExerciseUseCase;
    private final UpdateWorkoutExerciseUseCase updateWorkoutExerciseUseCase;
    private final DeleteExerciseFromWorkoutUseCase deleteExerciseFromWorkoutUseCase;

    @PostMapping
    @Operation(summary = "Add exercise to workout", description = "Adds an exercise to a workout")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Exercise added successfully"),
        @ApiResponse(responseCode = "404", description = "Workout or exercise not found")
    })
    public ResponseEntity<WorkoutExerciseResponse> create(@RequestBody CreateExerciseToWorkoutRequest request) {
        CreateExerciseToWorkoutCommand command = new CreateExerciseToWorkoutCommand(
            request.workoutId(), request.exerciseId(), request.position(),
            request.targetSets(), request.targetReps(), request.notes(), 
            request.sortOrder()
        );
        WorkoutExercise workoutExercise = createExerciseToWorkoutUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(WorkoutExerciseResponse.fromDomain(workoutExercise));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get workout exercise by ID", description = "Returns a single workout exercise by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Workout exercise found"),
        @ApiResponse(responseCode = "404", description = "Workout exercise not found")
    })
    public ResponseEntity<WorkoutExerciseResponse> findById(@PathVariable UUID id) {
        WorkoutExercise workoutExercise = getWorkoutExerciseUseCase.findById(id);
        return ResponseEntity.ok(WorkoutExerciseResponse.fromDomain(workoutExercise));
    }

    @GetMapping("/workout/{workoutId}")
    @Operation(summary = "List exercises by workout", description = "Returns all exercises for a specific workout")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<Page<WorkoutExerciseResponse>> findAllByWorkout(
        @PathVariable UUID workoutId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "position") String sort,
        @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageRequest.of(
        page, size,
        Sort.by(Sort.Direction.ASC, "sortOrder")
            .and(Sort.by(
                direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC,
                sort
            ))
    );

        Page<WorkoutExerciseResponse> exercises = getWorkoutExerciseUseCase.findAllByWorkout(workoutId, pageable)
            .map(WorkoutExerciseResponse::fromDomain);

        return ResponseEntity.ok(exercises);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update workout exercise", description = "Updates an existing workout exercise")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Workout exercise updated successfully"),
        @ApiResponse(responseCode = "404", description = "Workout exercise not found")
    })
    public ResponseEntity<WorkoutExerciseResponse> update(
        @PathVariable UUID id,
        @RequestBody UpdateWorkoutExerciseRequest request) 
    {
        UpdateWorkoutExerciseCommand command = new UpdateWorkoutExerciseCommand(
            id, request.position(), request.targetSets(), 
            request.targetReps(), request.notes(), request.sortOrder()
        );
        WorkoutExercise workoutExercise = updateWorkoutExerciseUseCase.execute(command);
        return ResponseEntity.ok(WorkoutExerciseResponse.fromDomain(workoutExercise));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove exercise from workout", description = "Removes an exercise from a workout")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Exercise removed successfully"),
        @ApiResponse(responseCode = "404", description = "Workout exercise not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteExerciseFromWorkoutUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}