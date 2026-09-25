package com.leo.estoque_api.config.security;

import com.leo.estoque_api.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

public class SecurityUtils {

    private SecurityUtils() {}

    public static UUID getAuthenticatedUserId() {
        User user = (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return user.getId();
    }

}
