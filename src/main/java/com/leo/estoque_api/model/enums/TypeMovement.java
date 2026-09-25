package com.leo.estoque_api.model.enums;

import lombok.Getter;

@Getter
public enum TypeMovement {

    CREATED("Created"),
    DEACTIVATE("Deactivate"),
    ENTRY("Entry"),
    EXIT("Exit"),
    ADJUSTMENT("Adjustment"),
    LOSS("Loss");

    private final String type;

    TypeMovement(String type) {
        this.type = type;
    }

}
