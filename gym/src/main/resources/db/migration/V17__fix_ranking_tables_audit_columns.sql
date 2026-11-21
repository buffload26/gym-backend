-- V17__fix_ranking_tables_audit_columns.sql
ALTER TABLE submissions
    RENAME COLUMN submitted_at TO created_at;

ALTER TABLE submissions
    DROP COLUMN reviewed_at,
    ADD COLUMN updated_at TIMESTAMP NOT NULL DEFAULT NOW();

ALTER TABLE submission_votes
    RENAME COLUMN voted_at TO created_at;