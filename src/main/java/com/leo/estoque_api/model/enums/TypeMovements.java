package com.leo.estoque_api.model.enums;

import lombok.Getter;

@Getter
public enum TypeMovements {

    ENTRY("Entry"),
    EXIT("Exit"),
    ADJUSTMENT("Adjustment"),
    REGISTRATION("Registration"),
    DEACTIVATION("Deactivation"),
    LOSS("Loss");

    private final String type;

    TypeMovements(String type) {
        this.type = type;
    }

}
