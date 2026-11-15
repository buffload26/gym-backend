package com.backend.gym.notification.infrastructure.web;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.gym.notification.application.port.in.GetNotificationsUseCase;
import com.backend.gym.notification.application.port.in.MarkNotificationReadUseCase;
import com.backend.gym.notification.infrastructure.web.dto.PushNotificationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Push notification management")
public class NotificationController {

    private final GetNotificationsUseCase getNotificationsUseCase;
    private final MarkNotificationReadUseCase markNotificationReadUseCase;

    @GetMapping("/users/{userId}")
    @Operation(summary = "List notifications", description = "Returns all notifications for a user")
    @ApiResponse(responseCode = "200", description = "List returned successfully")
    public ResponseEntity<Page<PushNotificationResponse>> findAllByUserId(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size,
            Sort.Direction.DESC, "createdAt");

        Page<PushNotificationResponse> notifications = getNotificationsUseCase
            .findAllByUserId(userId, pageable)
            .map(PushNotificationResponse::fromDomain);

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/users/{userId}/unread-count")
    @Operation(summary = "Count unread", description = "Returns the count of unread notifications")
    @ApiResponse(responseCode = "200", description = "Count returned successfully")
    public ResponseEntity<Long> countUnread(@PathVariable UUID userId) {
        return ResponseEntity.ok(
            getNotificationsUseCase.countUnreadByUserId(userId)
        );
    }

    @PatchMapping("/{notificationId}/read")
    @Operation(summary = "Mark as read", description = "Marks a notification as read")
    @ApiResponse(responseCode = "200", description = "Notification marked as read")
    public ResponseEntity<PushNotificationResponse> markAsRead(
            @PathVariable UUID notificationId) {

        return ResponseEntity.ok(
            PushNotificationResponse.fromDomain(
                markNotificationReadUseCase.execute(notificationId)
            )
        );
    }

    @PatchMapping("/users/{userId}/read-all")
    @Operation(summary = "Mark all as read", description = "Marks all notifications as read for a user")
    @ApiResponse(responseCode = "204", description = "All notifications marked as read")
    public ResponseEntity<Void> markAllAsRead(@PathVariable UUID userId) {
        markNotificationReadUseCase.executeAll(userId);
        return ResponseEntity.noContent().build();
    }
}