package com.backend.gym.exercise.infrastructure.web;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.gym.exercise.application.port.in.CreateExerciseUseCase;
import com.backend.gym.exercise.application.port.in.CreateExerciseUseCase.CreateExerciseCommand;
import com.backend.gym.exercise.application.port.in.GetExerciseUseCase;
import com.backend.gym.exercise.application.port.in.SoftDeleteExerciseUseCase;
import com.backend.gym.exercise.application.port.in.UpdateExerciseUseCase;
import com.backend.gym.exercise.application.port.in.UpdateExerciseUseCase.UpdateExerciseCommand;
import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.exercise.infrastructure.web.dto.CreateExerciseRequest;
import com.backend.gym.exercise.infrastructure.web.dto.ExerciseResponse;
import com.backend.gym.exercise.infrastructure.web.dto.UpdateExerciseRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/exercises")
@RequiredArgsConstructor
@Tag(name = "Exercises", description = "Exercise management")
public class ExerciseController {

    private final CreateExerciseUseCase createExerciseUseCase;
    private final GetExerciseUseCase getExerciseUseCase;
    private final UpdateExerciseUseCase updateExerciseUseCase;
    private final SoftDeleteExerciseUseCase softDeleteExerciseUseCase;

    @PostMapping
    @Operation(summary = "Create exercise", description = "Creates a new exercise")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Exercise created successfully")
    })
    public ResponseEntity<ExerciseResponse> create(@RequestBody CreateExerciseRequest request) {
        CreateExerciseCommand command = new CreateExerciseCommand(
            request.name(), request.description(), request.muscleGroup(),
            request.imageUrl(), request.videoUrl(), request.isDefault(),
            request.createdByUserId()
        );
        Exercise exercise = createExerciseUseCase.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(ExerciseResponse.fromDomain(exercise));
    }

    @GetMapping
    @Operation(summary = "List exercises", description = "Returns all active exercises")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<List<ExerciseResponse>> findAll() {
        List<ExerciseResponse> exercises = getExerciseUseCase.findAll().stream()
            .map(ExerciseResponse::fromDomain)
            .toList();
        return ResponseEntity.ok(exercises);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get exercise by ID", description = "Returns a single exercise by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Exercise found"),
        @ApiResponse(responseCode = "404", description = "Exercise not found")
    })
    public ResponseEntity<ExerciseResponse> findById(@PathVariable UUID id) {
        Exercise exercise = getExerciseUseCase.findById(id);
        return ResponseEntity.ok(ExerciseResponse.fromDomain(exercise));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update exercise", description = "Updates an existing exercise")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Exercise updated successfully"),
        @ApiResponse(responseCode = "404", description = "Exercise not found")
    })
    public ResponseEntity<ExerciseResponse> update(@PathVariable UUID id,
                                                   @RequestBody UpdateExerciseRequest request) {
        UpdateExerciseCommand command = new UpdateExerciseCommand(
            id, request.name(), request.description(), request.muscleGroup(),
            request.imageUrl(), request.videoUrl()
        );
        Exercise exercise = updateExerciseUseCase.execute(command);
        return ResponseEntity.ok(ExerciseResponse.fromDomain(exercise));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete exercise", description = "Soft deletes an exercise")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Exercise deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Exercise not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        softDeleteExerciseUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}