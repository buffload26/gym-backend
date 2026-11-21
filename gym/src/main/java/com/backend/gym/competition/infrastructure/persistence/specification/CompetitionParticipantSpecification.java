package com.backend.gym.competition.infrastructure.persistence.specification;

import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import com.backend.gym.competition.domain.CompetitionParticipantStatus;
import com.backend.gym.competition.infrastructure.persistence.CompetitionParticipantEntity;

public class CompetitionParticipantSpecification {

    public static Specification<CompetitionParticipantEntity> byCompetitionId(
            UUID competitionId
    ) {
        return (root, query, cb) -> competitionId == null ? null :
            cb.equal(root.get("competition").get("id"), competitionId);
    }

    public static Specification<CompetitionParticipantEntity> byUserId(
            UUID userId
    ) {
        return (root, query, cb) -> userId == null ? null :
            cb.equal(root.get("user").get("id"), userId);
    }

    public static Specification<CompetitionParticipantEntity> byStatus(
            CompetitionParticipantStatus status
    ) {
        return (root, query, cb) -> status == null ? null :
            cb.equal(root.get("status"), status);
    }
}