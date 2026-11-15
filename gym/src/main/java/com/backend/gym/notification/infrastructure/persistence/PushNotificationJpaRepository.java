package com.backend.gym.notification.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PushNotificationJpaRepository
        extends JpaRepository<PushNotificationEntity, UUID> {

    Page<PushNotificationEntity> findAllByUserId(UUID userId, Pageable pageable);

    @Query("""
        SELECT COUNT(pn) FROM PushNotificationEntity pn
        WHERE pn.user.id = :userId
        AND pn.readAt IS NULL
    """)
    long countUnreadByUserId(@Param("userId") UUID userId);

    @Modifying
    @Query("""
        UPDATE PushNotificationEntity pn
        SET pn.readAt = CURRENT_TIMESTAMP
        WHERE pn.user.id = :userId
        AND pn.readAt IS NULL
    """)
    void markAllAsReadByUserId(@Param("userId") UUID userId);
}