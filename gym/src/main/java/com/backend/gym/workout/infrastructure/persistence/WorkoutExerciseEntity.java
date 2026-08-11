package com.backend.gym.workout.infrastructure.persistence;

import java.util.List;
import java.util.UUID;

import com.backend.gym.exercise.domain.ExerciseWithLoads;
import com.backend.gym.exercise.infrastructure.persistence.ExerciseEntity;
import com.backend.gym.workout.domain.WorkoutExercise;

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
@Table(name = "workout_exercises")
public class WorkoutExerciseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_id", nullable = false)
    private WorkoutEntity workout;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExerciseEntity exercise;

    @Column
    private Integer position;

    @Column(name = "target_sets")
    private Integer targetSets;

    @Column(name = "target_reps", length = 30)
    private String targetReps;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Builder.Default
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    public WorkoutExercise toDomain() {
        return new WorkoutExercise(
            id,
            workout.toDomain(),
            new ExerciseWithLoads(exercise.toDomain(), List.of()),
            position,
            targetSets,
            targetReps,
            notes,
            sortOrder
        );
    }

    public static WorkoutExerciseEntity fromDomain(WorkoutExercise workoutExercise) {
        return WorkoutExerciseEntity.builder()
            .id(workoutExercise.id())
            .workout(WorkoutEntity.fromDomain(workoutExercise.workout()))
            .exercise(ExerciseEntity.fromDomain(workoutExercise.exercise().exercise()))
            .position(workoutExercise.position())
            .targetSets(workoutExercise.targetSets())
            .targetReps(workoutExercise.targetReps())
            .notes(workoutExercise.notes())
            .sortOrder(workoutExercise.sortOrder())
            .build();
    }
}