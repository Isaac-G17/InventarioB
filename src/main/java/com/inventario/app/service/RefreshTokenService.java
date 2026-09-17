package com.inventario.app.service;

import com.inventario.app.exception.InvalidTokenException;
import com.inventario.app.model.RefreshToken;
import com.inventario.app.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;
    private final long refreshTokenExpiration;

    public RefreshTokenService(RefreshTokenRepository repository,
                               @Value("${security.jwt.refresh-token-expiration-ms}") long refreshTokenExpiration) {
        this.repository = repository;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public RefreshToken create(String username) {
        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                username,
                Instant.now().plusMillis(refreshTokenExpiration));
        return repository.save(refreshToken);
    }

    public RefreshToken validate(String token) {
        if (token == null || token.isBlank()) {
            throw invalidToken();
        }

        RefreshToken refreshToken = repository.findById(token)
                .orElseThrow(() -> invalidToken());

        if (refreshToken.isRevoked() || refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw invalidToken();
        }
        return refreshToken;
    }

    public void revoke(RefreshToken refreshToken) {
        refreshToken.setRevoked(true);
        repository.save(refreshToken);
    }

    private InvalidTokenException invalidToken() {
        return new InvalidTokenException("El refresh token es inválido, fue revocado o expiró");
    }
}