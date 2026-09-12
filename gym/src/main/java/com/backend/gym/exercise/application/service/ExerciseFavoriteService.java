package com.backend.gym.exercise.application.service;

import com.backend.gym.exercise.application.port.in.GetExerciseFavoritesUseCase;
import com.backend.gym.exercise.application.port.in.ToggleExerciseFavoriteUseCase;
import com.backend.gym.exercise.application.port.out.ExerciseFavoriteRepositoryPort;
import com.backend.gym.exercise.domain.ExerciseFavorite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional 
@RequiredArgsConstructor
public class ExerciseFavoriteService implements
        ToggleExerciseFavoriteUseCase,
        GetExerciseFavoritesUseCase {

    private final ExerciseFavoriteRepositoryPort repository;

    @Override
    public void execute(ToggleExerciseFavoriteCommand command) {
        if (repository.existsByUserIdAndExerciseId(command.userId(), command.exerciseId())) {
            repository.delete(command.userId(), command.exerciseId());
        } else {
            repository.save(command.userId(), command.exerciseId());
        }
    }

    @Override
    public List<ExerciseFavorite> execute(UUID userId) {
        return repository.findAllByUserId(userId);
    }
}