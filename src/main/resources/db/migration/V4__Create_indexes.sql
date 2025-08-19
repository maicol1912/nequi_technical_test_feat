CREATE EXTENSION IF NOT EXISTS pg_trgm;

CREATE INDEX idx_branches_franchise_created
ON branches (franchise_id, created_at DESC);

-- Partial indexes for specific scenarios
CREATE INDEX idx_products_high_stock
ON products (branch_id, name)
WHERE stock > 0;

-- Full-text search indexes (if needed for future features)
CREATE INDEX IF NOT EXISTS idx_franchises_name_trgm
ON franchises USING gin (name gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_branches_name_trgm
ON branches USING gin (name gin_trgm_ops);

CREATE INDEX IF NOT EXISTS idx_products_name_trgm
ON products USING gin (name gin_trgm_ops);