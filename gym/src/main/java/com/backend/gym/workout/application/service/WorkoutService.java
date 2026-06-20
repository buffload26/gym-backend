package com.backend.gym.workout.application.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.shared.exception.workout.WorkoutNotFoundException;
import com.backend.gym.user.application.port.out.UserRepositoryPort;
import com.backend.gym.user.domain.User;
import com.backend.gym.workout.application.port.in.CreateWorkoutUseCase;
import com.backend.gym.workout.application.port.in.DeleteWorkoutUseCase;
import com.backend.gym.workout.application.port.in.GetWorkoutUseCase;
import com.backend.gym.workout.application.port.in.UpdateWorkoutUseCase;
import com.backend.gym.workout.application.port.out.WorkoutRepositoryPort;
import com.backend.gym.workout.domain.Workout;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkoutService implements
        CreateWorkoutUseCase,
        GetWorkoutUseCase,
        UpdateWorkoutUseCase,
        DeleteWorkoutUseCase {

    private final WorkoutRepositoryPort repository;
    private final UserRepositoryPort userRepository;

    @Override
    public Workout execute(CreateWorkoutCommand command) {
        User user = userRepository.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

        Workout workout = new Workout(
            null,
            user,
            command.name(),
            command.description(),
            command.imageUrl(),
            LocalDateTime.now(),
            LocalDateTime.now()
        );
        return repository.save(workout);
    }

    @Override
    public Workout findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new WorkoutNotFoundException(id));
    }

    @Override
    public Page<Workout> findAllByUser(UUID userId, Pageable pageable) {
        return repository.findAllByUserId(userId, pageable);
    }

    @Override
    public Workout execute(UpdateWorkoutCommand command) {
        Workout existing = findById(command.id());

        Workout updated = new Workout(
            existing.id(),
            existing.user(),
            command.name(),
            command.description(),
            command.imageUrl(),
            existing.createdAt(),
            LocalDateTime.now()
        );
        return repository.save(updated);
    }

    @Override
    public void execute(UUID id) {
        findById(id);
        repository.deleteById(id);
    }
}