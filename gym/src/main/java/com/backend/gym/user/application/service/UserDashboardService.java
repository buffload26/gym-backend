package com.backend.gym.user.application.service;

import com.backend.gym.exercise.application.port.out.ExerciseFavoriteRepositoryPort;
import com.backend.gym.exercise.domain.Exercise;
import com.backend.gym.exercise.domain.ExerciseFavorite;
import com.backend.gym.exercise.domain.ExerciseWithLoads;
import com.backend.gym.loadentry.application.port.out.LoadEntryRepositoryPort;
import com.backend.gym.loadentry.domain.LoadEntry;
import com.backend.gym.user.application.port.in.GetUserDashboardUseCase;
import com.backend.gym.user.domain.UserDashboard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDashboardService implements GetUserDashboardUseCase {

    private final LoadEntryRepositoryPort loadEntryRepository;
    private final ExerciseFavoriteRepositoryPort exerciseFavoriteRepository;

    @Override
    public UserDashboard execute(UUID userId) {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year = today.getYear();

        int exercisesThisMonth = loadEntryRepository
            .countByUserIdAndMonth(userId, month, year);

        BigDecimal totalLoadThisMonth = loadEntryRepository
            .sumLoadKgByUserIdAndMonth(userId, month, year);

        int currentStreak = calculateStreak(
            loadEntryRepository.findDistinctPerformedDatesByUserId(userId)
        );

        List<LoadEntry> lastFiveLoadEntries = loadEntryRepository.findLastFiveByUserId(userId);

        Map<UUID, List<LoadEntry>> loadsByExerciseId = lastFiveLoadEntries.stream()
            .collect(Collectors.groupingBy(load -> load.exercise().id()));

        List<ExerciseWithLoads> lastFiveExercises = lastFiveLoadEntries.stream()
            .map(LoadEntry::exercise)
            .distinct()
            .limit(5)
            .map(exercise -> new ExerciseWithLoads(
                exercise,
                loadsByExerciseId.getOrDefault(exercise.id(), List.of())
            ))
            .toList();
        
        List<Exercise> favoriteExercises = exerciseFavoriteRepository.findAllByUserId(userId)
                                            .stream().map(ExerciseFavorite::exercise).toList();

        return new UserDashboard(
            exercisesThisMonth,
            totalLoadThisMonth,
            currentStreak,
            lastFiveExercises,
            favoriteExercises
        );
    }

    private int calculateStreak(List<LocalDate> dates) {
        if (dates.isEmpty()) return 0;

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);

        if (!dates.contains(today) && !dates.contains(yesterday)) return 0;

        int streak = 0;
        LocalDate expected = dates.contains(today) ? today : yesterday;

        for (LocalDate date : dates) {
            if (date.equals(expected)) {
                streak++;
                expected = expected.minusDays(1);
            } else if (date.isBefore(expected)) {
                break;
            }
        }

        return streak;
    }
}