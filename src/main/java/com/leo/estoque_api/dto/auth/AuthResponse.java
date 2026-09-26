package com.leo.estoque_api.dto.auth;

import com.leo.estoque_api.dto.user.UserResponse;

public record AuthResponse(
        String accessToken,
        String tokenType,
        UserResponse user
) {
}
