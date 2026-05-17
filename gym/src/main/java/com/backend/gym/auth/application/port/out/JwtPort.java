package com.backend.gym.auth.application.port.out;

public interface JwtPort {
    String generateAccessToken(String email);
    String generateRefreshToken(String email);
    String extractEmail(String token);
    String extractTokenType(String token);
    boolean isTokenValid(String token);
}