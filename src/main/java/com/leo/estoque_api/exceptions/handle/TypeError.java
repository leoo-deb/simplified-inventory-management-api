package com.leo.estoque_api.exceptions.handle;

import lombok.Getter;

@Getter
public enum TypeError {

    ENTITY_NOT_FOUND("Entity not found"),
    CREDENTIALS_INVALID("Invalidated credentials."),
    BUSINESS_ROLE_VIOLATION("Business Rule Violation"),
    CONFLICT("Data Conflict"),
    SYSTEM_ERROR("Server error"),
    INVALID_DATA("Invalid Data"),
    INVALID_PARAMETER("Invalid Parameter"),
    INVALID_BODY("Invalid Body");

    private final String type;

    TypeError(String type) {
        this.type = type;
    }

}
