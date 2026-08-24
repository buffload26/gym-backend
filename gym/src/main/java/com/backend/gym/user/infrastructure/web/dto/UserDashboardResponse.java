package com.backend.gym.user.infrastructure.web.dto;

import com.backend.gym.exercise.infrastructure.web.dto.ExerciseResponse;
import com.backend.gym.exercise.infrastructure.web.dto.ExerciseWithLoadsResponse;
import com.backend.gym.user.domain.UserDashboard;

import java.math.BigDecimal;
import java.util.List;

public record UserDashboardResponse(
    int exercisesThisMonth,
    BigDecimal totalLoadThisMonth,
    int currentStreak,
    List<ExerciseWithLoadsResponse> lastFiveExercises,
    List<ExerciseResponse> favoriteExercises
) {
    public static UserDashboardResponse fromDomain(UserDashboard dashboard) {
        return new UserDashboardResponse(
            dashboard.exercisesThisMonth(),
            dashboard.totalLoadThisMonth(),
            dashboard.currentStreak(),
            dashboard.lastFiveExercises().stream()
                .map(ExerciseWithLoadsResponse::fromDomain)
                .toList(),
            dashboard.favoriteExercises().stream()
                .map(ExerciseResponse::fromDomain)
                .toList()
        );
    }
}