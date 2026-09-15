package com.leo.estoque_api.exceptions;

public class ConflictException extends  BusinessRuleException {

    public ConflictException(String message) {
        super(message);
    }

    public ConflictException(String message, Throwable cause) {
        super(message, cause);
    }

}
