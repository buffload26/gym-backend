-- V18__update_submissions_and_drop_rankings.sql
ALTER TABLE submissions
    ADD COLUMN load_kg NUMERIC(6,2) NOT NULL DEFAULT 0,
    ADD COLUMN repetitions INTEGER,
    ADD COLUMN observation TEXT;

DROP TABLE rankings;