package com.backend.gym.exercise.infrastructure.persistence;

import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class ExerciseSpecification {

    public static Specification<ExerciseEntity> isNotDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deletedAt"));
    }

    public static Specification<ExerciseEntity> byUserId(UUID userId) {
        return (root, query, cb) -> userId == null ? null :
            cb.or(
                cb.equal(root.get("createdByUser").get("id"), userId),
                cb.isTrue(root.get("isDefault"))
            );
    }

    public static Specification<ExerciseEntity> byMuscleGroup(String muscleGroup) {
        return (root, query, cb) -> muscleGroup == null ? null :
            cb.like(cb.lower(root.get("muscleGroup")), "%" + muscleGroup.toLowerCase() + "%");
    }
}
