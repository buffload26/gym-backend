package com.backend.gym.ranking.infrastructure.web;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.gym.ranking.application.port.in.CreateSubmissionUseCase;
import com.backend.gym.ranking.application.port.in.CreateSubmissionUseCase.CreateSubmissionCommand;
import com.backend.gym.ranking.application.port.in.GetSubmissionUseCase;
import com.backend.gym.ranking.domain.Submission;
import com.backend.gym.ranking.domain.SubmissionStatus;
import com.backend.gym.ranking.infrastructure.web.dto.CreateSubmissionRequest;
import com.backend.gym.ranking.infrastructure.web.dto.SubmissionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/submissions")
@RequiredArgsConstructor
@Tag(name = "Submissions", description = "Competition submission management")
public class SubmissionController {

    private final CreateSubmissionUseCase createSubmissionUseCase;
    private final GetSubmissionUseCase getSubmissionUseCase;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create submission", description = "Creates a new submission with video")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Submission created successfully"),
        @ApiResponse(responseCode = "400", description = "User already has a submission"),
        @ApiResponse(responseCode = "404", description = "Competition or user not found")
    })
    public ResponseEntity<SubmissionResponse> create(
            @ModelAttribute CreateSubmissionRequest request) {

        Submission submission = createSubmissionUseCase.execute(new CreateSubmissionCommand(
            request.competitionId(),
            request.userId(),
            request.video()
        ));

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(SubmissionResponse.fromDomain(submission));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get submission by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Submission found"),
        @ApiResponse(responseCode = "404", description = "Submission not found")
    })
    public ResponseEntity<SubmissionResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.ok(
            SubmissionResponse.fromDomain(getSubmissionUseCase.findById(id))
        );
    }

    @GetMapping("/filter")
    @Operation(summary = "Filter submissions", description = "Returns submissions filtered by competition and/or status")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<Page<SubmissionResponse>> findAllByFilter(
            @RequestParam UUID competitionId,
            @RequestParam(required = false) SubmissionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "submittedAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        Pageable pageable = PageRequest.of(
            page,
            size,
            direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC,
            sort
        );

        Page<SubmissionResponse> submissions = status != null
            ? getSubmissionUseCase.findAllByCompetitionAndStatus(competitionId, status, pageable)
                .map(SubmissionResponse::fromDomain)
            : getSubmissionUseCase.findAllByCompetition(competitionId, pageable)
                .map(SubmissionResponse::fromDomain);

        return ResponseEntity.ok(submissions);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete submission")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Submission deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Submission not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        getSubmissionUseCase.findById(id);
        // delegado ao service via use case de delete quando implementado
        return ResponseEntity.noContent().build();
    }
}