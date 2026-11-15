
package com.backend.gym.competition.infrastructure.web;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.gym.competition.application.port.in.AcceptCompetitionInviteUseCase;
import com.backend.gym.competition.application.port.in.CreateCompetitionUseCase;
import com.backend.gym.competition.application.port.in.CreateCompetitionUseCase.CreateCompetitionCommand;
import com.backend.gym.competition.application.port.in.DeleteCompetitionUseCase;
import com.backend.gym.competition.application.port.in.FinishCompetitionUseCase;
import com.backend.gym.competition.application.port.in.GetCompetitionUseCase;
import com.backend.gym.competition.application.port.in.GetCompetitionUseCase.CompetitionFilter;
import com.backend.gym.competition.application.port.in.InviteCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.InviteCompetitionParticipantUseCase.InviteCompetitionParticipantCommand;
import com.backend.gym.competition.application.port.in.JoinCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.JoinCompetitionParticipantUseCase.JoinCompetitionParticipantCommand;
import com.backend.gym.competition.application.port.in.LeaveCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.RejectCompetitionInviteUseCase;
import com.backend.gym.competition.application.port.in.UpdateCompetitionUseCase;
import com.backend.gym.competition.application.port.in.UpdateCompetitionUseCase.UpdateCompetitionCommand;
import com.backend.gym.competition.domain.Competition;
import com.backend.gym.competition.domain.CompetitionParticipant;
import com.backend.gym.competition.domain.CompetitionStatus;
import com.backend.gym.competition.domain.CompetitionWithParticipants;
import com.backend.gym.competition.infrastructure.web.dto.CompetitionDetailResponse;
import com.backend.gym.competition.infrastructure.web.dto.CompetitionParticipantResponse;
import com.backend.gym.competition.infrastructure.web.dto.CompetitionResponse;
import com.backend.gym.competition.infrastructure.web.dto.CreateCompetitionRequest;
import com.backend.gym.competition.infrastructure.web.dto.JoinCompetitionParticipantRequest;
import com.backend.gym.competition.infrastructure.web.dto.UpdateCompetitionRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/competitions")
@RequiredArgsConstructor
@Tag(name = "Competitions", description = "Competition management")
public class CompetitionController {

    private final CreateCompetitionUseCase createCompetitionUseCase;
    private final GetCompetitionUseCase getCompetitionUseCase;
    private final UpdateCompetitionUseCase updateCompetitionUseCase;
    private final DeleteCompetitionUseCase deleteCompetitionUseCase;
    private final FinishCompetitionUseCase finishCompetitionUseCase;

    private final JoinCompetitionParticipantUseCase joinCompetitionParticipantUseCase;
    private final LeaveCompetitionParticipantUseCase leaveCompetitionParticipantUseCase;
    private final InviteCompetitionParticipantUseCase inviteCompetitionParticipantUseCase;
    private final AcceptCompetitionInviteUseCase acceptCompetitionInviteUseCase;
    private final RejectCompetitionInviteUseCase rejectCompetitionInviteUseCase;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create competition", description = "Creates a new competition")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Competition created successfully")
    })
    public ResponseEntity<CompetitionResponse> create(
            @ModelAttribute CreateCompetitionRequest request) {

        CreateCompetitionCommand command = new CreateCompetitionCommand(
            request.title(),
            request.description(),
            request.rules(),
            request.image(),
            request.exerciseId(),
            request.createdByUserId(),
            request.startAt(),
            request.endAt()
        );

        Competition competition = createCompetitionUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CompetitionResponse.fromDomain(competition));
    }

    @GetMapping
    @Operation(summary = "List competitions", description = "Returns all active competitions")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<Page<CompetitionResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageRequest.of(
            page,
            size,
            direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC,
            sort
        );

        Page<CompetitionResponse> competitions = getCompetitionUseCase
            .findAll(pageable)
            .map(c -> CompetitionResponse.fromDomain(c.competition(), c.participantCount()));

        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/filter")
    @Operation(
        summary = "Filter competitions",
        description = "Returns competitions filtered by user, exercise and/or status"
    )
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<Page<CompetitionResponse>> findAllByFilter(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) UUID exerciseId,
            @RequestParam(required = false) CompetitionStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        Pageable pageable = PageRequest.of(
            page,
            size,
            direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC,
            sort
        );

        CompetitionFilter filter = new CompetitionFilter(
            userId,
            exerciseId,
            status
        );

        Page<CompetitionResponse> competitions = getCompetitionUseCase
            .findAllByFilter(filter, pageable)
            .map(c -> CompetitionResponse.fromDomain(c.competition(), c.participantCount()));

        return ResponseEntity.ok(competitions);
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get competition by ID",
        description = "Returns a single competition by its ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Competition found"),
        @ApiResponse(responseCode = "404", description = "Competition not found")
    })
    public ResponseEntity<CompetitionDetailResponse> findById(
            @PathVariable UUID id) {

        CompetitionWithParticipants competition = getCompetitionUseCase.findByIdWithParticipants(id);

        return ResponseEntity.ok(CompetitionDetailResponse.fromDomain(competition));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
        summary = "Update competition",
        description = "Updates an existing competition"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Competition updated successfully"),
        @ApiResponse(responseCode = "404", description = "Competition not found")
    })
    public ResponseEntity<CompetitionResponse> update(
            @PathVariable UUID id,
            @ModelAttribute UpdateCompetitionRequest request) {

        UpdateCompetitionCommand command = new UpdateCompetitionCommand(
            id,
            request.title(),
            request.description(),
            request.rules(),
            request.image(),
            request.startAt(),
            request.endAt()
        );

        Competition competition = updateCompetitionUseCase.execute(command);

        return ResponseEntity.ok(CompetitionResponse.fromDomain(competition));
    }

    @PatchMapping("/{id}/finish")
    @Operation(
        summary = "Finish competition",
        description = "Changes the competition status to FINISHED"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Competition finished successfully"),
        @ApiResponse(responseCode = "404", description = "Competition not found")
    })
    public ResponseEntity<CompetitionResponse> finish(
            @PathVariable UUID id) {

        Competition competition = finishCompetitionUseCase.finish(id);

        return ResponseEntity.ok(CompetitionResponse.fromDomain(competition));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete competition",
        description = "Soft deletes a competition"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Competition deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Competition not found")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteCompetitionUseCase.execute(id);

        return ResponseEntity.noContent().build();
    }

        @PostMapping("/{competitionId}/participants")
    @Operation(
        summary = "Join competition",
        description = "Adds a user as a participant in a competition"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Participant added successfully"),
        @ApiResponse(responseCode = "404", description = "Competition or user not found"),
        @ApiResponse(responseCode = "409", description = "User is already a participant or competition is not active")
    })
    public ResponseEntity<CompetitionParticipantResponse> join(
            @PathVariable UUID competitionId,
            @RequestBody JoinCompetitionParticipantRequest request) {

        JoinCompetitionParticipantCommand command = new JoinCompetitionParticipantCommand(
            competitionId,
            request.userId()
        );

        CompetitionParticipant participant = joinCompetitionParticipantUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CompetitionParticipantResponse.fromDomain(participant));
    }

    @DeleteMapping("/{competitionId}/participants/{userId}")
    @Operation(
        summary = "Leave competition",
        description = "Removes a user from a competition"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Participant removed successfully"),
        @ApiResponse(responseCode = "404", description = "Participant not found")
    })
    public ResponseEntity<Void> leave(
            @PathVariable UUID competitionId,
            @PathVariable UUID userId) {

        leaveCompetitionParticipantUseCase.execute(competitionId, userId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{competitionId}/participants/invite")
    @Operation(summary = "Invite participant", description = "Invites a user to a competition")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Invite sent successfully"),
        @ApiResponse(responseCode = "404", description = "Competition or user not found"),
        @ApiResponse(responseCode = "409", description = "User is already a participant")
    })
    public ResponseEntity<CompetitionParticipantResponse> invite(
            @PathVariable UUID competitionId,
            @RequestBody JoinCompetitionParticipantRequest request) {

        InviteCompetitionParticipantCommand command = new InviteCompetitionParticipantCommand(
            competitionId,
            request.userId()
        );

        CompetitionParticipant participant = inviteCompetitionParticipantUseCase.execute(command);

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(CompetitionParticipantResponse.fromDomain(participant));
    }

    @PatchMapping("/{competitionId}/participants/{userId}/accept")
    @Operation(summary = "Accept invite", description = "Accepts a competition invite")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Invite accepted successfully"),
        @ApiResponse(responseCode = "404", description = "Invite not found")
    })
    public ResponseEntity<CompetitionParticipantResponse> accept(
            @PathVariable UUID competitionId,
            @PathVariable UUID userId) {

        CompetitionParticipant participant = acceptCompetitionInviteUseCase.accept(
            competitionId, userId
        );

        return ResponseEntity.ok(CompetitionParticipantResponse.fromDomain(participant));
    }

    @PatchMapping("/{competitionId}/participants/{userId}/reject")
    @Operation(summary = "Reject invite", description = "Rejects a competition invite")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Invite rejected successfully"),
        @ApiResponse(responseCode = "404", description = "Invite not found")
    })
    public ResponseEntity<CompetitionParticipantResponse> reject(
            @PathVariable UUID competitionId,
            @PathVariable UUID userId) {

        CompetitionParticipant participant = rejectCompetitionInviteUseCase.reject(
            competitionId, userId
        );

        return ResponseEntity.ok(CompetitionParticipantResponse.fromDomain(participant));
    }
}
