-- V13__alter_competition_participants_joined_at_nullable.sql
ALTER TABLE competition_participants
    ALTER COLUMN joined_at DROP NOT NULL;