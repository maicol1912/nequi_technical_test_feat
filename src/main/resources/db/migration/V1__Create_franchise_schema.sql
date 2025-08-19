CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS franchises (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_franchises_name_unique ON franchises (LOWER(name));

CREATE INDEX IF NOT EXISTS idx_franchises_created_at ON franchises (created_at DESC);

ALTER TABLE franchises
    ADD CONSTRAINT chk_franchises_name_not_empty
    CHECK (length(trim(name)) >= 2);

COMMENT ON TABLE franchises IS 'Stores franchise information';
COMMENT ON COLUMN franchises.id IS 'Unique identifier for the franchise';
COMMENT ON COLUMN franchises.name IS 'Name of the franchise (must be unique)';
COMMENT ON COLUMN franchises.created_at IS 'Timestamp when the franchise was created';
COMMENT ON COLUMN franchises.updated_at IS 'Timestamp when the franchise was last updated';