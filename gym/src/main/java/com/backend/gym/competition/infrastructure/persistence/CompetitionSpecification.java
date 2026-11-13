
package com.backend.gym.competition.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.backend.gym.competition.domain.CompetitionStatus;

public class CompetitionSpecification {

    public static Specification<CompetitionEntity> isNotDeleted() {
        return (root, query, cb) ->
            cb.isNull(root.get("deletedAt"));
    }

    public static Specification<CompetitionEntity> byUserId(UUID userId) {
        return (root, query, cb) -> userId == null ? null :
            cb.equal(root.get("createdByUser").get("id"), userId);
    }

    public static Specification<CompetitionEntity> byExerciseId(UUID exerciseId) {
        return (root, query, cb) -> exerciseId == null ? null :
            cb.equal(root.get("exercise").get("id"), exerciseId);
    }

    public static Specification<CompetitionEntity> byStatus(CompetitionStatus status) {
        return (root, query, cb) -> status == null ? null :
            cb.equal(root.get("status"), status);
    }
}
