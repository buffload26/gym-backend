-- V19__remove_submission_unique_constraint.sql
ALTER TABLE submissions
    DROP CONSTRAINT submissions_competition_id_user_id_key;