package com.leo.estoque_api.exceptions;

import java.util.UUID;

public class ProductVariantNotFoundException extends EntityNotFoundException {
    public ProductVariantNotFoundException(String sku) {
        super(String.format("Product variant with SKU '%s' not found.", sku));
    }

    public ProductVariantNotFoundException(UUID id) {
        super(String.format("Product variant with id '%s' not found.", id));
    }
}
