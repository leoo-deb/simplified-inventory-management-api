package com.leo.estoque_api.exceptions;

import java.util.UUID;

public class PhotoVariantNotFoundException extends EntityNotFoundException {
    public PhotoVariantNotFoundException(UUID productId, UUID variantId) {
        super(String.format("There is no Photo of the code Variant '%s' of the code Product '%s'.", variantId, productId));
    }
}
