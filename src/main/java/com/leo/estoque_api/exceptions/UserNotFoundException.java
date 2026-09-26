package com.leo.estoque_api.exceptions;

import java.util.UUID;

public class UserNotFoundException extends EntityNotFoundException {

    public UserNotFoundException(UUID id) {
        super(String.format("There is no User with code '%s'.", id));
    }

    public UserNotFoundException(String email) {
        super(String.format("There is no User with e-mail '%s'.", email));
    }
}
