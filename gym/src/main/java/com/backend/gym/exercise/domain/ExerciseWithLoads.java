package com.backend.gym.exercise.domain;

import java.util.List;
import com.backend.gym.loadentry.domain.LoadEntry;

public record ExerciseWithLoads(
    Exercise exercise,
    List<LoadEntry> loads
) {}