package com.backend.gym.loadentry.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import com.backend.gym.exercise.infrastructure.persistence.ExerciseEntity;
import com.backend.gym.loadentry.domain.LoadEntry;
import com.backend.gym.user.infrastructure.persistence.UserEntity;
import com.backend.gym.workout.infrastructure.persistence.WorkoutEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "load_entries")
public class LoadEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExerciseEntity exercise;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id")
    private WorkoutEntity workout;

    @Column(name = "performed_at", nullable = false)
    private LocalDate performedAt;

    @Column(name = "load_kg", nullable = false, precision = 6, scale = 2)
    private BigDecimal loadKg;

    @Column(name = "warmup_load_kg", precision = 6, scale = 2)
    private BigDecimal warmupLoadKg;

    @Column(name = "next_load_kg", precision = 6, scale = 2)
    private BigDecimal nextLoadKg;

    @Column
    private Integer sets;

    @Column
    private Integer reps;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public LoadEntry toDomain() {
        return new LoadEntry(
            id,
            user.toDomain(),
            exercise.toDomain(),
            workout != null ? workout.toDomain() : null,
            performedAt,
            loadKg,
            warmupLoadKg,
            nextLoadKg, 
            sets,
            reps,
            notes,
            createdAt
        );
    }

    public static LoadEntryEntity fromDomain(LoadEntry loadEntry,
                                             UserEntity userEntity,
                                             ExerciseEntity exerciseEntity,
                                             WorkoutEntity workoutEntity) {
        return LoadEntryEntity.builder()
            .id(loadEntry.id())
            .user(userEntity)
            .exercise(exerciseEntity)
            .workout(workoutEntity)
            .performedAt(loadEntry.performedAt())
            .loadKg(loadEntry.loadKg())
            .warmupLoadKg(loadEntry.warmupLoadKg())
            .nextLoadKg(loadEntry.nextLoadKg())
            .sets(loadEntry.sets())
            .reps(loadEntry.reps())
            .notes(loadEntry.notes())
            .createdAt(loadEntry.createdAt())
            .build();
    }
}
