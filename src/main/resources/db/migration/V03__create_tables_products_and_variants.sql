CREATE TABLE "tb_products" (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               category_id UUID NOT NULL,
                               name VARCHAR(50) NOT NULL,
                               image_base_url VARCHAR(500),
                               description TEXT NOT NULL,
                               active BOOLEAN NOT NULL DEFAULT true,
                               created_at TIMESTAMP NOT NULL DEFAULT now()
);

ALTER TABLE tb_products
    ADD CONSTRAINT fk_products_categories FOREIGN KEY (category_id) REFERENCES tb_categories(id);

CREATE INDEX idx_products_categories ON tb_products(category_id);
CREATE INDEX idx_products_name ON tb_products(name);


CREATE TABLE "tb_products_variants" (
                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                               product_id UUID NOT NULL,
                               model VARCHAR(40) NOT NULL,
                               sku VARCHAR(20) UNIQUE NOT NULL,
                               image_url VARCHAR(500),
                               stock BIGINT NOT NULL,
                               minimum_stock BIGINT NOT NULL,
                               price NUMERIC(19, 4),
                               observation TEXT,
                               active BOOLEAN NOT NULL DEFAULT true,
                               created_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE INDEX idx_variants_products ON tb_products_variants(product_id);
CREATE INDEX idx_variants_sku ON tb_products_variants(sku);
CREATE INDEX idx_variants_stock ON tb_products_variants(stock);
CREATE INDEX idx_variants_minimum_stock ON tb_products_variants(minimum_stock);
