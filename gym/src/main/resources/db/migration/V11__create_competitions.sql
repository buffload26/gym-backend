-- V11__create_competitions.sql
CREATE TABLE competitions (
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title              VARCHAR(120) NOT NULL,
    description        TEXT,
    rules              TEXT,
    image_url          TEXT,
    exercise_id        UUID NOT NULL REFERENCES exercises(id),
    created_by_user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    status             VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    start_at           TIMESTAMP NOT NULL,
    end_at             TIMESTAMP,
    deleted_at         TIMESTAMP,
    created_at         TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at         TIMESTAMP NOT NULL DEFAULT NOW(),
    CHECK (end_at > start_at),
    CHECK (status IN ('ACTIVE', 'FINISHED'))
);

CREATE TABLE competition_participants (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    competition_id UUID NOT NULL REFERENCES competitions(id) ON DELETE CASCADE,
    user_id        UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    joined_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (competition_id, user_id)
);

CREATE INDEX idx_competitions_status ON competitions (status);
CREATE INDEX idx_participants_user ON competition_participants (user_id);