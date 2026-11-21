package com.backend.gym.ranking.infrastructure.web;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.gym.ranking.application.port.in.GetSubmissionVoteUseCase;
import com.backend.gym.ranking.application.port.in.VoteSubmissionUseCase;
import com.backend.gym.ranking.application.port.in.VoteSubmissionUseCase.VoteSubmissionCommand;
import com.backend.gym.ranking.domain.SubmissionVote;
import com.backend.gym.ranking.infrastructure.web.dto.SubmissionVoteResponse;
import com.backend.gym.ranking.infrastructure.web.dto.VoteSubmissionRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/submissions/{submissionId}/votes")
@RequiredArgsConstructor
@Tag(name = "Submission Votes", description = "Submission vote management")
public class SubmissionVoteController {

    private final VoteSubmissionUseCase voteSubmissionUseCase;
    private final GetSubmissionVoteUseCase getSubmissionVoteUseCase;

    @PostMapping
    @Operation(summary = "Vote on submission", description = "Accept or reject a submission")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Vote registered successfully"),
        @ApiResponse(responseCode = "400", description = "User already voted or invalid vote"),
        @ApiResponse(responseCode = "404", description = "Submission not found")
    })
    public ResponseEntity<SubmissionVoteResponse> vote(
            @PathVariable UUID submissionId,
            @RequestBody VoteSubmissionRequest request) {

        SubmissionVote vote = voteSubmissionUseCase.execute(new VoteSubmissionCommand(
            submissionId,
            request.voterUserId(),
            request.vote()
        ));

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(SubmissionVoteResponse.fromDomain(vote));
    }

    @GetMapping
    @Operation(summary = "List votes", description = "Returns all votes for a submission")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<Page<SubmissionVoteResponse>> findAll(
            @PathVariable UUID submissionId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        Pageable pageable = PageRequest.of(
            page,
            size,
            direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC,
            sort
        );

        Page<SubmissionVoteResponse> votes = getSubmissionVoteUseCase
            .findAllBySubmission(submissionId, pageable)
            .map(SubmissionVoteResponse::fromDomain);

        return ResponseEntity.ok(votes);
    }
}