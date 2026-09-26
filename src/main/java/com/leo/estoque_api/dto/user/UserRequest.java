package com.leo.estoque_api.dto.user;

import com.leo.estoque_api.model.enums.UserRole;

public record UserRequest(
        String name,
        String email,
        String password,
        UserRole role
) {
}
