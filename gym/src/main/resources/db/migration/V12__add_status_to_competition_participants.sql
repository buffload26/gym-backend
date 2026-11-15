-- V12__add_status_to_competition_participants.sql
ALTER TABLE competition_participants
    ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ACCEPTED',
    ADD COLUMN invited_at TIMESTAMP;

UPDATE competition_participants SET status = 'ACCEPTED';