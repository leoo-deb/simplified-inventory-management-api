package com.leo.estoque_api.dto.user;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        String role,
        Boolean emailVerified,
        Boolean active,
        OffsetDateTime createdAt
) {
}
