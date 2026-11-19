package com.backend.gym.competition.infrastructure.web;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.gym.competition.application.port.in.AcceptCompetitionInviteUseCase;
import com.backend.gym.competition.application.port.in.GetCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.GetCompetitionParticipantUseCase.CompetitionParticipantFilter;
import com.backend.gym.competition.application.port.in.InviteCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.InviteCompetitionParticipantUseCase.InviteCompetitionParticipantCommand;
import com.backend.gym.competition.application.port.in.JoinCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.JoinCompetitionParticipantUseCase.JoinCompetitionParticipantCommand;
import com.backend.gym.competition.application.port.in.LeaveCompetitionParticipantUseCase;
import com.backend.gym.competition.application.port.in.RejectCompetitionInviteUseCase;
import com.backend.gym.competition.domain.CompetitionParticipant;
import com.backend.gym.competition.domain.CompetitionParticipantStatus;
import com.backend.gym.competition.infrastructure.web.dto.CompetitionParticipantResponse;
import com.backend.gym.competition.infrastructure.web.dto.JoinCompetitionParticipantRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/competitions/{competitionId}/participants")
@RequiredArgsConstructor
@Tag(name = "Competition Participants", description = "Competition participant management")
public class CompetitionParticipantController {

    private final JoinCompetitionParticipantUseCase joinCompetitionParticipantUseCase;
    private final LeaveCompetitionParticipantUseCase leaveCompetitionParticipantUseCase;
    private final InviteCompetitionParticipantUseCase inviteCompetitionParticipantUseCase;
    private final AcceptCompetitionInviteUseCase acceptCompetitionInviteUseCase;
    private final RejectCompetitionInviteUseCase rejectCompetitionInviteUseCase;
    private final GetCompetitionParticipantUseCase getCompetitionParticipantUseCase;

    @PostMapping
    @Operation(summary = "Join competition", description = "Adds a user as a participant in a competition")
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

    @DeleteMapping("/{userId}")
    @Operation(summary = "Leave competition", description = "Removes a user from a competition")
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

    @PostMapping("/invite")
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

    @PatchMapping("/{userId}/accept")
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

    @PatchMapping("/{userId}/reject")
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