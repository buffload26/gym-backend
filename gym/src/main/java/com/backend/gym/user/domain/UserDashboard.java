package com.backend.gym.user.domain;

import com.backend.gym.exercise.domain.Exercise;

import java.math.BigDecimal;
import java.util.List;

public record UserDashboard(
    int exercisesThisMonth,
    BigDecimal totalLoadThisMonth,
    int currentStreak,
    List<Exercise> lastFiveExercises
) {}