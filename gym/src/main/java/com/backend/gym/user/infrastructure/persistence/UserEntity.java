package com.backend.gym.user.infrastructure.persistence;

import com.backend.gym.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, unique = true, length = 180)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public User toDomain() {
        return new User(id, name, email, passwordHash, createdAt, updatedAt);
    }

    public static UserEntity fromDomain(User user) {
        return UserEntity.builder()
            .id(user.id())
            .name(user.name())
            .email(user.email())
            .passwordHash(user.passwordHash())
            .createdAt(user.createdAt())
            .updatedAt(user.updatedAt())
            .build();
    }
}