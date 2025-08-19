CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    branch_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    stock INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_products_branch
        FOREIGN KEY (branch_id)
        REFERENCES branches(id)
        ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_products_branch_name_unique
ON products (branch_id, LOWER(name));

CREATE INDEX IF NOT EXISTS idx_products_branch_id ON products (branch_id);
CREATE INDEX IF NOT EXISTS idx_products_name ON products (name);
CREATE INDEX IF NOT EXISTS idx_products_stock ON products (stock DESC);
CREATE INDEX IF NOT EXISTS idx_products_created_at ON products (created_at DESC);

ALTER TABLE products
    ADD CONSTRAINT chk_products_name_not_empty
    CHECK (length(trim(name)) >= 2);

ALTER TABLE products
    ADD CONSTRAINT chk_products_stock_non_negative
    CHECK (stock >= 0);

COMMENT ON TABLE products IS 'Stores product information for branches';
COMMENT ON COLUMN products.id IS 'Unique identifier for the product';
COMMENT ON COLUMN products.branch_id IS 'Reference to the parent branch';
COMMENT ON COLUMN products.name IS 'Name of the product (must be unique within branch)';
COMMENT ON COLUMN products.stock IS 'Current stock quantity (non-negative)';
COMMENT ON COLUMN products.created_at IS 'Timestamp when the product was created';
COMMENT ON COLUMN products.updated_at IS 'Timestamp when the product was last updated';