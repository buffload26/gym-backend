ALTER TABLE users
    ADD COLUMN verification_code VARCHAR(6),
    ADD COLUMN verification_code_expires_at TIMESTAMP,
    ADD COLUMN is_verified BOOLEAN NOT NULL DEFAULT FALSE;