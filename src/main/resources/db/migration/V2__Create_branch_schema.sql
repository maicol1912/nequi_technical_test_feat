CREATE TABLE IF NOT EXISTS branches (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    franchise_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_branches_franchise
        FOREIGN KEY (franchise_id)
        REFERENCES franchises(id)
        ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_branches_franchise_name_unique
ON branches (franchise_id, LOWER(name));

CREATE INDEX IF NOT EXISTS idx_branches_franchise_id ON branches (franchise_id);
CREATE INDEX IF NOT EXISTS idx_branches_name ON branches (name);
CREATE INDEX IF NOT EXISTS idx_branches_created_at ON branches (created_at DESC);

ALTER TABLE branches
    ADD CONSTRAINT chk_branches_name_not_empty
    CHECK (length(trim(name)) >= 2);

COMMENT ON TABLE branches IS 'Stores branch information for franchises';
COMMENT ON COLUMN branches.id IS 'Unique identifier for the branch';
COMMENT ON COLUMN branches.franchise_id IS 'Reference to the parent franchise';
COMMENT ON COLUMN branches.name IS 'Name of the branch (must be unique within franchise)';
COMMENT ON COLUMN branches.created_at IS 'Timestamp when the branch was created';
COMMENT ON COLUMN branches.updated_at IS 'Timestamp when the branch was last updated';