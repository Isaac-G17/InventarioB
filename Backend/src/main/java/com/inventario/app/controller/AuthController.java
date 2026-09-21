package com.inventario.app.controller;

import com.inventario.app.dto.AuthResponse;
import com.inventario.app.dto.LoginRequest;
import com.inventario.app.dto.RefreshRequest;
import com.inventario.app.model.RefreshToken;
import com.inventario.app.security.JwtService;
import com.inventario.app.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticación", description = "Login y renovación de tokens")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final long accessTokenExpiration;

    public AuthController(AuthenticationManager authenticationManager,
                          UserDetailsService userDetailsService,
                          JwtService jwtService,
                          RefreshTokenService refreshTokenService,
                          @Value("${security.jwt.access-token-expiration-ms}") long accessTokenExpiration) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.accessTokenExpiration = accessTokenExpiration;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Valida usuario y contraseña y entrega los tokens")
    public AuthResponse login(@RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        return issueTokens(request.username());
    }

    @PostMapping("/refresh")
    @Operation(summary = "Renovar tokens", description = "Entrega un nuevo par de tokens usando el refresh token")
    public AuthResponse refresh(@RequestBody RefreshRequest request) {
        RefreshToken current = refreshTokenService.validate(request.refreshToken());
        refreshTokenService.revoke(current);
        return issueTokens(current.getUsername());
    }

    private AuthResponse issueTokens(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        String accessToken = jwtService.generateAccessToken(user);
        RefreshToken refreshToken = refreshTokenService.create(username);
        return new AuthResponse(accessToken, refreshToken.getToken(), accessTokenExpiration / 1000);
    }
}
