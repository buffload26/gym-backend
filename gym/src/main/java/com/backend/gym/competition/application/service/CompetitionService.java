
package com.backend.gym.competition.application.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.gym.competition.application.port.in.DeleteCompetitionUseCase;
import com.backend.gym.competition.application.port.in.FinishCompetitionUseCase;
import com.backend.gym.competition.application.port.in.CreateCompetitionUseCase;
import com.backend.gym.competition.application.port.in.GetCompetitionUseCase;
import com.backend.gym.competition.application.port.in.UpdateCompetitionUseCase;
import com.backend.gym.competition.application.port.out.CompetitionParticipantRepositoryPort;
import com.backend.gym.competition.application.port.out.CompetitionRepositoryPort;
import com.backend.gym.competition.domain.Competition;
import com.backend.gym.competition.domain.CompetitionParticipant;
import com.backend.gym.competition.domain.CompetitionParticipantStatus;
import com.backend.gym.competition.domain.CompetitionStatus;
import com.backend.gym.competition.domain.CompetitionWithParticipantCount;
import com.backend.gym.competition.domain.CompetitionWithParticipants;
import com.backend.gym.exercise.application.port.out.ExerciseRepositoryPort;
import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.shared.exception.competition.CompetitionNotFoundException;
import com.backend.gym.shared.exception.exercise.ExerciseNotFoundException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.storage.application.port.out.FileStoragePort;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompetitionService implements
        CreateCompetitionUseCase,
        GetCompetitionUseCase,
        UpdateCompetitionUseCase,
        DeleteCompetitionUseCase,
        FinishCompetitionUseCase {

    private final CompetitionRepositoryPort repository;
    private final ExerciseRepositoryPort exerciseRepository;
    private final UserRepositoryPort userRepository;
    private final CompetitionParticipantRepositoryPort participantRepository;
    private final FileStoragePort fileStoragePort;

    @Override
    @Transactional
    public Competition execute(CreateCompetitionCommand command) {
        validateDates(command.startAt(), command.endAt());

        Exercise exercise = exerciseRepository.findById(command.exerciseId())
            .orElseThrow(() -> new ExerciseNotFoundException(command.exerciseId()));

        User createdByUser = null;

        if (command.createdByUserId() != null) {
            createdByUser = userRepository.findById(command.createdByUserId())
                .orElseThrow(() -> new UserNotFoundException(command.createdByUserId()));
        }

        String imageUrl = null;

        if (command.image() != null && !command.image().isEmpty()) {
            imageUrl = fileStoragePort.upload(command.image());
        }

        Competition competition = new Competition(
            null,
            command.title(),
            command.description(),
            command.rules(),
            imageUrl,
            exercise,
            createdByUser,
            CompetitionStatus.ACTIVE,
            command.startAt(),
            command.endAt(),
            null,
            LocalDateTime.now(),
            LocalDateTime.now()
        );

        Competition saved = repository.save(competition);

        if (createdByUser != null) {
            CompetitionParticipant participant = new CompetitionParticipant(
                null,
                saved,
                createdByUser,
                CompetitionParticipantStatus.ACCEPTED,
                LocalDateTime.now(),
                null
            );
            participantRepository.save(participant);
        }

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public CompetitionWithParticipantCount findById(UUID id) {
        Competition competition = repository.findById(id)
            .orElseThrow(() -> new CompetitionNotFoundException(id));

        long count = participantRepository.countByCompetitionId(id);

        return new CompetitionWithParticipantCount(competition, count);
    }

    @Override
    @Transactional()
    public CompetitionWithParticipants findByIdWithParticipants(UUID id) {
        Competition competition = findById(id).competition();

        List<User> participants =
            participantRepository.findAllByCompetitionId(id).stream().map(CompetitionParticipant::user).toList();

        return new CompetitionWithParticipants(competition, participants);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompetitionWithParticipantCount> findAll(Pageable pageable) {
        Page<Competition> page = repository.findAllActive(pageable);
        return toPageWithCount(page);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompetitionWithParticipantCount> findAllByFilter(
            CompetitionFilter filter,
            Pageable pageable) {

        Page<Competition> page = repository.findAllByFilter(
            filter.userId(),
            filter.exerciseId(),
            filter.status(),
            pageable
        );

        return toPageWithCount(page);
    }

    @Override
    @Transactional
    public Competition execute(UpdateCompetitionCommand command) {
        Competition existing = findById(command.id()).competition();

        validateDates(command.startAt(), command.endAt());

        String newImageUrl = existing.imageUrl();

        if (command.image() != null && !command.image().isEmpty()) {
            newImageUrl = fileStoragePort.replaceFile(
                command.image(),
                existing.imageUrl()
            );
        }

        Competition updated = new Competition(
            existing.id(),
            command.title(),
            command.description(),
            command.rules(),
            newImageUrl,
            existing.exercise(),
            existing.createdByUser(),
            existing.status(),
            command.startAt(),
            command.endAt(),
            existing.deletedAt(),
            existing.createdAt(),
            LocalDateTime.now()
        );

        return repository.save(updated);
    }

    @Override
    @Transactional
    public void execute(UUID id) {
        Competition existing = findById(id).competition();

        Competition deleted = new Competition(
            existing.id(),
            existing.title(),
            existing.description(),
            existing.rules(),
            existing.imageUrl(),
            existing.exercise(),
            existing.createdByUser(),
            existing.status(),
            existing.startAt(),
            existing.endAt(),
            LocalDateTime.now(),
            existing.createdAt(),
            LocalDateTime.now()
        );

        repository.save(deleted);
    }

    @Override
    @Transactional
    public Competition finish(UUID id) {
        Competition existing = findById(id).competition();

        if (existing.status() == CompetitionStatus.FINISHED) {
            throw new IllegalStateException("Competition is already finished");
        }

        Competition finished = new Competition(
            existing.id(),
            existing.title(),
            existing.description(),
            existing.rules(),
            existing.imageUrl(),
            existing.exercise(),
            existing.createdByUser(),
            CompetitionStatus.FINISHED,
            existing.startAt(),
            existing.endAt(),
            existing.deletedAt(),
            existing.createdAt(),
            LocalDateTime.now()
        );

        return repository.save(finished);
    }

    private void validateDates(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("Competition start date is required");
        }

        if (endAt != null && !endAt.isAfter(startAt)) {
            throw new IllegalArgumentException(
                "Competition end date must be after start date"
            );
        }
    }

    private Page<CompetitionWithParticipantCount> toPageWithCount(Page<Competition> page) {
        List<UUID> ids = page.getContent().stream()
            .map(Competition::id)
            .toList();

        Map<UUID, Long> countMap = participantRepository.countByCompetitionIds(ids);

        List<CompetitionWithParticipantCount> content = page.getContent().stream()
            .map(c -> new CompetitionWithParticipantCount(
                c,
                countMap.getOrDefault(c.id(), 0L)
            ))
            .toList();

        return new PageImpl<>(content, page.getPageable(), page.getTotalElements());
    }
}
