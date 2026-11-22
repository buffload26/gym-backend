package com.backend.gym.submission.infrastructure.persistence.specification;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.backend.gym.submission.domain.SubmissionStatus;
import com.backend.gym.submission.infrastructure.persistence.SubmissionEntity;

public class SubmissionSpecification {

    public static Specification<SubmissionEntity> byCompetitionId(UUID competitionId) {
        return (root, query, cb) -> competitionId == null ? null :
            cb.equal(root.get("competition").get("id"), competitionId);
    }

    public static Specification<SubmissionEntity> byUserId(UUID userId) {
        return (root, query, cb) -> userId == null ? null :
            cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<SubmissionEntity> byStatus(SubmissionStatus status) {
        return (root, query, cb) -> status == null ? null :
            cb.equal(root.get("status"), status);
    }
}