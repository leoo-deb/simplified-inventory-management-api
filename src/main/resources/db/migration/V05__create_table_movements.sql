CREATE TABLE "tb_movements" (
                                id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                variant_id UUID NOT NULL,
                                user_id UUID NOT NULL,
                                type VARCHAR(20) NOT NULL,
                                quantity BIGINT UNIQUE NOT NULL,
                                old_stock BIGINT NOT NULL,
                                new_stock BIGINT NOT NULL,
                                description TEXT,
                                date_time TIMESTAMP NOT NULL DEFAULT now()
);

ALTER TABLE tb_movements
    ADD CONSTRAINT fk_movements_variants FOREIGN KEY (variant_id) REFERENCES tb_products_variants(id);

ALTER TABLE tb_movements
    ADD CONSTRAINT fk_movements_user FOREIGN KEY (user_id) REFERENCES tb_users(id);

CREATE INDEX idx_movements_variant_id ON tb_movements(variant_id);
CREATE INDEX idx_movements_user_id ON tb_movements(user_id);
CREATE INDEX idx_movements_quantity ON tb_movements(quantity);