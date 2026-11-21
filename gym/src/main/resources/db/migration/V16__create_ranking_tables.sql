-- V16__create_ranking_tables.sql
CREATE TABLE submissions (
    id             UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    competition_id UUID        NOT NULL REFERENCES competitions(id) ON DELETE CASCADE,
    user_id        UUID        NOT NULL REFERENCES users(id),
    video_url      TEXT        NOT NULL,
    video_deleted_at TIMESTAMP,
    status         VARCHAR(20) NOT NULL DEFAULT 'PENDING'
                       CHECK (status IN ('PENDING', 'ACCEPTED', 'REJECTED')),
    submitted_at   TIMESTAMP   NOT NULL DEFAULT NOW(),
    reviewed_at    TIMESTAMP,
    UNIQUE (competition_id, user_id)
);

CREATE TABLE submission_votes (
    id            UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    submission_id UUID        NOT NULL REFERENCES submissions(id) ON DELETE CASCADE,
    voter_user_id UUID        NOT NULL REFERENCES users(id),
    vote          VARCHAR(10) NOT NULL CHECK (vote IN ('ACCEPT', 'REJECT')),
    voted_at      TIMESTAMP   NOT NULL DEFAULT NOW(),
    UNIQUE (submission_id, voter_user_id)
);

CREATE TABLE rankings (
    id             UUID    PRIMARY KEY DEFAULT gen_random_uuid(),
    competition_id UUID    NOT NULL REFERENCES competitions(id) ON DELETE CASCADE,
    user_id        UUID    NOT NULL REFERENCES users(id),
    score          INTEGER NOT NULL DEFAULT 0,
    position       INTEGER,
    updated_at     TIMESTAMP NOT NULL DEFAULT NOW(),
    UNIQUE (competition_id, user_id)
);

CREATE INDEX idx_submissions_competition ON submissions(competition_id);
CREATE INDEX idx_submissions_user        ON submissions(user_id);
CREATE INDEX idx_submissions_status      ON submissions(status);
CREATE INDEX idx_votes_submission        ON submission_votes(submission_id);
CREATE INDEX idx_rankings_competition    ON rankings(competition_id);
CREATE INDEX idx_rankings_score          ON rankings(competition_id, score DESC);