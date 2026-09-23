package com.backend.gym.workout.application.service;

import java.time.LocalDateTime;
import java.util.List;
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
import com.backend.gym.workout.application.port.in.ReorderWorkoutUseCase;
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
        DeleteWorkoutUseCase,
        ReorderWorkoutUseCase {

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
            0,
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
            existing.sortOrder(),
            command.imageUrl(),
            existing.createdAt(),
            LocalDateTime.now()
        );
        return repository.save(updated);
    }

    @Override
    public void execute(UUID id) {
        Workout existing = findById(id);
        int deletedOrder = existing.sortOrder();

        repository.deleteById(id);

        List<Workout> allWorkouts = repository.findAllByUserIdOrdered(existing.user().id());

        for (Workout w : allWorkouts) {
            if (w.sortOrder() > deletedOrder) {
                repository.save(new Workout(
                    w.id(), w.user(), 
                    w.name(), w.description(),
                    w.sortOrder() - 1, w.imageUrl(),
                    w.createdAt(), LocalDateTime.now()
                ));
            }
        }
    }

    @Override
    public void execute(ReorderWorkoutCommand command) {
        Workout existing = findById(command.workoutId());

        int oldOrder = existing.sortOrder();
        int newOrder = command.newSortOrder();

        if (oldOrder == newOrder) return;

        List<Workout> allWorkouts = repository.findAllByUserIdOrdered(existing.user().id());

        for (Workout w : allWorkouts) {
            if (w.id().equals(command.workoutId())) continue;

            int currentOrder = w.sortOrder();
            int adjustedOrder = currentOrder;

            if (newOrder < oldOrder) {
                if (currentOrder >= newOrder && currentOrder < oldOrder) {
                    adjustedOrder = currentOrder + 1;
                }
            } else {
                if (currentOrder > oldOrder && currentOrder <= newOrder) {
                    adjustedOrder = currentOrder - 1;
                }
            }

            if (adjustedOrder != currentOrder) {
                repository.save(new Workout(
                    w.id(), w.user(), 
                    w.name(), w.description(),
                    adjustedOrder, w.imageUrl(),
                    w.createdAt(), LocalDateTime.now()
                ));
            }
        }

        repository.save(new Workout(
            existing.id(), existing.user(), 
            existing.name(), existing.description(),
            newOrder, existing.imageUrl(),
            existing.createdAt(), LocalDateTime.now()
        ));
    }
}