package com.inventario.app.dto;

public record AuthResponse(String accessToken, String refreshToken, long expiresIn) {
}
