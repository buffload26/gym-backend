package com.backend.gym.exercise.infrastructure.persistence;

import com.backend.gym.exercise.application.port.out.ExerciseFavoriteRepositoryPort;
import com.backend.gym.exercise.domain.ExerciseFavorite;
import com.backend.gym.shared.exception.exercise.ExerciseNotFoundException;
import com.backend.gym.shared.exception.user.UserNotFoundException;
import com.backend.gym.user.infrastructure.persistence.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ExerciseFavoriteRepositoryAdapter implements ExerciseFavoriteRepositoryPort {

    private final ExerciseFavoriteJpaRepository jpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final ExerciseJpaRepository exerciseJpaRepository;

    @Override
    public ExerciseFavorite save(UUID userId, UUID exerciseId) {
        var userEntity = userJpaRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        var exerciseEntity = exerciseJpaRepository.findById(exerciseId)
            .orElseThrow(() -> new ExerciseNotFoundException(exerciseId));

        var entity = ExerciseFavoriteEntity.builder()
            .user(userEntity)
            .exercise(exerciseEntity)
            .createdAt(LocalDateTime.now())
            .build();

        return jpaRepository.save(entity).toDomain();
    }

    @Override
    public void delete(UUID userId, UUID exerciseId) {
        jpaRepository.deleteByUserIdAndExerciseId(userId, exerciseId);
    }

    @Override
    public List<ExerciseFavorite> findAllByUserId(UUID userId) {
        return jpaRepository.findAllByUserId(userId).stream()
            .map(ExerciseFavoriteEntity::toDomain)
            .toList();
    }

    @Override
    public boolean existsByUserIdAndExerciseId(UUID userId, UUID exerciseId) {
        return jpaRepository.existsByUserIdAndExerciseId(userId, exerciseId);
    }
}