package com.leo.estoque_api.dto.user;

public record UserUpdateRequest(
        String name,
        String email
) {
}
