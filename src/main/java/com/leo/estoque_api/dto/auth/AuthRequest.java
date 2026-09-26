package com.leo.estoque_api.dto.auth;

public record AuthRequest(
        String email,
        String password
) {
}
