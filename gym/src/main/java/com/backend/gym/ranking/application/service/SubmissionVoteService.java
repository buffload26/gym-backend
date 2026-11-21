package com.backend.gym.ranking.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.gym.competition.application.port.out.CompetitionParticipantRepositoryPort;
import com.backend.gym.ranking.application.port.in.GetSubmissionVoteUseCase;
import com.backend.gym.ranking.application.port.in.VoteSubmissionUseCase;
import com.backend.gym.ranking.application.port.out.SubmissionRepositoryPort;
import com.backend.gym.ranking.application.port.out.SubmissionVoteRepositoryPort;
import com.backend.gym.ranking.domain.Submission;
import com.backend.gym.ranking.domain.SubmissionStatus;
import com.backend.gym.ranking.domain.SubmissionVote;
import com.backend.gym.ranking.domain.SubmissionVoteType;
import com.backend.gym.shared.exception.ranking.SubmissionAlreadyReviewedException;
import com.backend.gym.shared.exception.ranking.SubmissionNotFoundException;
import com.backend.gym.shared.exception.ranking.SubmissionSelfVoteException;
import com.backend.gym.shared.exception.ranking.SubmissionVoteAlreadyExistsException;
import com.backend.gym.shared.exception.ranking.SubmissionVoterNotParticipantException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.storage.application.port.out.FileStoragePort;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubmissionVoteService implements
        VoteSubmissionUseCase,
        GetSubmissionVoteUseCase {

    private final SubmissionVoteRepositoryPort voteRepository;
    private final SubmissionRepositoryPort submissionRepository;
    private final CompetitionParticipantRepositoryPort participantRepository;
    private final UserRepositoryPort userRepository;
    private final FileStoragePort fileStoragePort;

    @Override
    @Transactional
    public SubmissionVote execute(VoteSubmissionCommand command) {
        Submission existing = submissionRepository.findById(command.submissionId())
            .orElseThrow(() -> new SubmissionNotFoundException(command.submissionId()));

        if (existing.status() != SubmissionStatus.PENDING) {
            throw new SubmissionAlreadyReviewedException();
        }

        if (existing.user().id().equals(command.voterUserId())) {
            throw new SubmissionSelfVoteException();
        }

        if (!participantRepository.existsByCompetitionIdAndUserId(
                existing.competition().id(), command.voterUserId())) {
            throw new SubmissionVoterNotParticipantException();
        }

        if (voteRepository.existsBySubmissionIdAndVoterId(
                command.submissionId(), command.voterUserId())) {
            throw new SubmissionVoteAlreadyExistsException();
        }

        User voter = userRepository.findById(command.voterUserId())
            .orElseThrow(() -> new UserNotFoundException(command.voterUserId()));

        SubmissionVote vote = new SubmissionVote(
            null,
            existing,
            voter,
            command.vote(),
            LocalDateTime.now()
        );

        SubmissionVote saved = voteRepository.save(vote);

        checkQuorumAndReview(existing);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SubmissionVote> findAllBySubmission(UUID submissionId, Pageable pageable) {
        return voteRepository.findAllBySubmissionId(submissionId, pageable);
    }

    private void checkQuorumAndReview(Submission submission) {
        long totalParticipants = participantRepository.countByCompetitionId(
            submission.competition().id()
        );
        long totalVotes = voteRepository.countBySubmissionId(submission.id());

        if (totalVotes < Math.ceil(totalParticipants * 0.5)) {
            return;
        }

        long acceptVotes = voteRepository.countBySubmissionIdAndVote(
            submission.id(), SubmissionVoteType.ACCEPT
        );
        long rejectVotes = voteRepository.countBySubmissionIdAndVote(
            submission.id(), SubmissionVoteType.REJECT
        );

        SubmissionStatus newStatus = rejectVotes > acceptVotes
            ? SubmissionStatus.REJECTED
            : SubmissionStatus.ACCEPTED;

        Submission reviewed = new Submission(
            submission.id(),
            submission.competition(),
            submission.user(),
            submission.videoUrl(),
            null,
            newStatus,
            submission.createdAt(),
            LocalDateTime.now()
        );

        submissionRepository.save(reviewed);

        fileStoragePort.delete(submission.videoUrl());

        submissionRepository.save(new Submission(
            reviewed.id(),
            reviewed.competition(),
            reviewed.user(),
            reviewed.videoUrl(),
            LocalDateTime.now(),
            reviewed.status(),
            reviewed.createdAt(),
            LocalDateTime.now()
        ));
    }
}