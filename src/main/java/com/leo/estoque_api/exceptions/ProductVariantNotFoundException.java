package com.leo.estoque_api.exceptions;

import java.util.UUID;

public class ProductVariantNotFoundException extends EntityNotFoundException {
    public ProductVariantNotFoundException(UUID id, String sku) {
        super(String.format("Variant with SKU '%s' for product with code '%s' not found.", sku, id));
    }

    public ProductVariantNotFoundException(UUID id) {
        super(String.format("Variant with code '%s' not found.", id));
    }

    public ProductVariantNotFoundException(UUID productId, UUID variantId) {
        super(String.format("There is no code Variant '%s' for Product code '%s'.", variantId, productId));
    }
}
